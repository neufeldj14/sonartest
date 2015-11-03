describe.skip('Application', function () {
  describe('Severity Comparators', function () {
    describe('#severityComparator', function () {
      it('should have correct order', function () {
        assert.equal(window.severityComparator('BLOCKER'), 0);
        assert.equal(window.severityComparator('CRITICAL'), 1);
        assert.equal(window.severityComparator('MAJOR'), 2);
        assert.equal(window.severityComparator('MINOR'), 3);
        assert.equal(window.severityComparator('INFO'), 4);
      });
    });

    describe('#severityColumnsComparator', function () {
      it('should have correct order', function () {
        assert.equal(window.severityColumnsComparator('BLOCKER'), 0);
        assert.equal(window.severityColumnsComparator('CRITICAL'), 2);
        assert.equal(window.severityColumnsComparator('MAJOR'), 4);
        assert.equal(window.severityColumnsComparator('MINOR'), 1);
        assert.equal(window.severityColumnsComparator('INFO'), 3);
      });
    });
  });
});
