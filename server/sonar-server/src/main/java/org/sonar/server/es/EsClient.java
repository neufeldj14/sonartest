/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.es;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequestBuilder;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.flush.FlushRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.optimize.OptimizeRequestBuilder;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequestBuilder;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Priority;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.picocontainer.Startable;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.server.es.request.ProxyBulkRequestBuilder;
import org.sonar.server.es.request.ProxyClearCacheRequestBuilder;
import org.sonar.server.es.request.ProxyClusterHealthRequestBuilder;
import org.sonar.server.es.request.ProxyClusterStateRequestBuilder;
import org.sonar.server.es.request.ProxyClusterStatsRequestBuilder;
import org.sonar.server.es.request.ProxyCountRequestBuilder;
import org.sonar.server.es.request.ProxyCreateIndexRequestBuilder;
import org.sonar.server.es.request.ProxyDeleteByQueryRequestBuilder;
import org.sonar.server.es.request.ProxyDeleteRequestBuilder;
import org.sonar.server.es.request.ProxyFlushRequestBuilder;
import org.sonar.server.es.request.ProxyGetRequestBuilder;
import org.sonar.server.es.request.ProxyIndexRequestBuilder;
import org.sonar.server.es.request.ProxyIndicesExistsRequestBuilder;
import org.sonar.server.es.request.ProxyIndicesStatsRequestBuilder;
import org.sonar.server.es.request.ProxyMultiGetRequestBuilder;
import org.sonar.server.es.request.ProxyNodesStatsRequestBuilder;
import org.sonar.server.es.request.ProxyPutMappingRequestBuilder;
import org.sonar.server.es.request.ProxyRefreshRequestBuilder;
import org.sonar.server.es.request.ProxySearchRequestBuilder;
import org.sonar.server.es.request.ProxySearchScrollRequestBuilder;
import org.sonar.server.search.SearchClient;

/**
 * Facade to connect to Elasticsearch node. Handles correctly errors (logging + exceptions
 * with context) and profiling of requests.
 */
public class EsClient implements Startable {

  public static final Logger LOGGER = Loggers.get("es");
  private final SearchClient deprecatedClient;

  public EsClient(SearchClient deprecatedClient) {
    this.deprecatedClient = deprecatedClient;
  }

  public RefreshRequestBuilder prepareRefresh(String... indices) {
    return new ProxyRefreshRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public FlushRequestBuilder prepareFlush(String... indices) {
    return new ProxyFlushRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public IndicesStatsRequestBuilder prepareStats(String... indices) {
    return new ProxyIndicesStatsRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public NodesStatsRequestBuilder prepareNodesStats(String... nodesIds) {
    return new ProxyNodesStatsRequestBuilder(deprecatedClient.nativeClient()).setNodesIds(nodesIds);
  }

  public ClusterStatsRequestBuilder prepareClusterStats() {
    return new ProxyClusterStatsRequestBuilder(deprecatedClient.nativeClient());
  }

  public ClusterStateRequestBuilder prepareState() {
    return new ProxyClusterStateRequestBuilder(deprecatedClient.nativeClient());
  }

  public ClusterHealthRequestBuilder prepareHealth(String... indices) {
    return new ProxyClusterHealthRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public void waitForStatus(ClusterHealthStatus status) {
    prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForStatus(status).get();
  }

  public IndicesExistsRequestBuilder prepareIndicesExist(String... indices) {
    return new ProxyIndicesExistsRequestBuilder(deprecatedClient.nativeClient(), indices);
  }

  public CreateIndexRequestBuilder prepareCreate(String index) {
    return new ProxyCreateIndexRequestBuilder(deprecatedClient.nativeClient(), index);
  }

  public PutMappingRequestBuilder preparePutMapping(String... indices) {
    return new ProxyPutMappingRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public SearchRequestBuilder prepareSearch(String... indices) {
    return new ProxySearchRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public SearchScrollRequestBuilder prepareSearchScroll(String scrollId) {
    return new ProxySearchScrollRequestBuilder(scrollId, deprecatedClient.nativeClient());
  }

  public GetRequestBuilder prepareGet() {
    return new ProxyGetRequestBuilder(deprecatedClient.nativeClient());
  }

  public GetRequestBuilder prepareGet(String index, String type, String id) {
    return new ProxyGetRequestBuilder(deprecatedClient.nativeClient()).setIndex(index).setType(type).setId(id);
  }

  public MultiGetRequestBuilder prepareMultiGet() {
    return new ProxyMultiGetRequestBuilder(deprecatedClient.nativeClient());
  }

  public CountRequestBuilder prepareCount(String... indices) {
    return new ProxyCountRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public BulkRequestBuilder prepareBulk() {
    return new ProxyBulkRequestBuilder(deprecatedClient.nativeClient());
  }

  public DeleteRequestBuilder prepareDelete(String index, String type, String id) {
    return new ProxyDeleteRequestBuilder(deprecatedClient.nativeClient(), index).setType(type).setId(id);
  }

  public DeleteByQueryRequestBuilder prepareDeleteByQuery(String... indices) {
    return new ProxyDeleteByQueryRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public IndexRequestBuilder prepareIndex(String index, String type) {
    return new ProxyIndexRequestBuilder(deprecatedClient.nativeClient()).setIndex(index).setType(type);
  }

  public OptimizeRequestBuilder prepareOptimize(String indexName) {
    // TODO add proxy for profiling
    return nativeClient().admin().indices().prepareOptimize(indexName)
      .setMaxNumSegments(1);
  }

  public ClearIndicesCacheRequestBuilder prepareClearCache(String... indices) {
    return new ProxyClearCacheRequestBuilder(deprecatedClient.nativeClient()).setIndices(indices);
  }

  public long getMaxFieldValue(String indexName, String typeName, String fieldName) {
    SearchRequestBuilder request = prepareSearch(indexName)
      .setTypes(typeName)
      .setQuery(QueryBuilders.matchAllQuery())
      .setSize(0)
      .addAggregation(AggregationBuilders.max("latest").field(fieldName));

    Max max = request.get().getAggregations().get("latest");
    return (long) max.getValue();
  }

  @Override
  public void start() {
    // nothing to do
  }

  @Override
  public void stop() {
    // TODO re-enable when SearchClient is dropped
    // client.close();
  }

  protected Client nativeClient() {
    return deprecatedClient.nativeClient();
  }
}
