import 'package:explorer_ui/src/providers/search_query_provider.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  test('SearchQuery must initialize well', () {
    // With query set to null, it won't compile:
    // The argument type 'Null' can't be assigned to the parameter type 'String'
    // SearchQuery(null);

    // Testing an empty query.
    var emptyQuery = SearchQuery("");
    assert(emptyQuery.isEmpty());
    assert(emptyQuery.query == "");
    assert(emptyQuery.pageNumber == 1);

    // Testing with a query.
    var existingQueryPage1 = SearchQuery("a");
    assert(!existingQueryPage1.isEmpty());
    assert(existingQueryPage1.query == "a");
    assert(existingQueryPage1.pageNumber == 1);

    // Testing with a query.
    var existingQueryPage2 = SearchQuery("a", pageNumber: 2);
    assert(!existingQueryPage2.isEmpty());
    assert(existingQueryPage2.query == "a");
    assert(existingQueryPage2.pageNumber == 2);

    // Testing queries with wrong page numbers.
    expect(() => SearchQuery("test", pageNumber: -1), throwsAssertionError);
    expect(() => SearchQuery("test", pageNumber: 0), throwsAssertionError);
  });
}
