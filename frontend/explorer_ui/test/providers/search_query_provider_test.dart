import 'package:explorer_ui/src/providers/search_query_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

final searchQueryProvider = StateProvider((ref) => SearchQueryNotifier());

void main() {
  test('SearchQuery provider behavior', () {
    final container = ProviderContainer();

    // Initial state
    var initialState = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(initialState.isEmpty());
    assert(initialState.query == "");
    assert(initialState.page == 1);

    // Make a search
    container.read(searchQueryProvider.notifier).state.setQuery("test");
    var firstSearch = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(!firstSearch.isEmpty());
    assert(firstSearch.query == "test");
    assert(firstSearch.page == 1);

    // Change page
    container.read(searchQueryProvider.notifier).state.setPage(2);
    var firstSearchAndPageChange = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(!firstSearchAndPageChange.isEmpty());
    assert(firstSearchAndPageChange.query == "test");
    assert(firstSearchAndPageChange.page == 2);

    // Make a new search
    container.read(searchQueryProvider.notifier).state.setQuery("test2");
    var secondSearch = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(!secondSearch.isEmpty());
    assert(secondSearch.query == "test2");
    assert(secondSearch.page == 1);

    // Change everything
    container.read(searchQueryProvider.notifier).state.setQueryAndPage("test3", 3);
    var thirdSearch = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(!thirdSearch.isEmpty());
    assert(thirdSearch.query == "test3");
    assert(thirdSearch.page == 3);

    // Try to change to wrong page number
    expect(() => container.read(searchQueryProvider.notifier).state.setPage(-1), throwsAssertionError);
    expect(() => container.read(searchQueryProvider.notifier).state.setPage(0), throwsAssertionError);
  });

  test('SearchQuery class behavior', () {
    // With query set to null, it won't compile:
    // The argument type 'Null' can't be assigned to the parameter type 'String'
    // SearchQuery(null);

    // Testing an empty query.
    var emptyQueryWithConstructor = SearchQuery.empty();
    assert(emptyQueryWithConstructor.isEmpty());
    assert(emptyQueryWithConstructor.query == "");
    assert(emptyQueryWithConstructor.page == 1);

    // Testing an empty query.
    var emptyQuery = SearchQuery("");
    assert(emptyQuery.isEmpty());
    assert(emptyQuery.query == "");
    assert(emptyQuery.page == 1);

    // Testing with a query.
    var existingQueryPage1 = SearchQuery("a");
    assert(!existingQueryPage1.isEmpty());
    assert(existingQueryPage1.query == "a");
    assert(existingQueryPage1.page == 1);

    // Testing with a query.
    var existingQueryPage2 = SearchQuery("a", page: 2);
    assert(!existingQueryPage2.isEmpty());
    assert(existingQueryPage2.query == "a");
    assert(existingQueryPage2.page == 2);

    // Testing queries with wrong page numbers.
    expect(() => SearchQuery("test", page: -1), throwsAssertionError);
    expect(() => SearchQuery("test", page: 0), throwsAssertionError);
  });
}
