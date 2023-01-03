import 'package:explorer_ui/src/providers/asset_search_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

final assetSearchQueryProvider = StateProvider((ref) => AssetSearchQueryNotifier());

void main() {
  test('AssetSearchQuery provider behavior', () {
    final container = ProviderContainer();

    // Initial state
    var initialState = container.read(assetSearchQueryProvider.notifier).state.getQuery();
    assert(initialState.isEmpty());
    assert(initialState.query == "");
    assert(initialState.page == 1);

    // Make a search
    container.read(assetSearchQueryProvider.notifier).state.setQuery("test");
    var firstSearch = container.read(assetSearchQueryProvider.notifier).state.getQuery();
    assert(!firstSearch.isEmpty());
    assert(firstSearch.query == "test");
    assert(firstSearch.page == 1);

    // Change page
    container.read(assetSearchQueryProvider.notifier).state.setPage(2);
    var firstSearchAndPageChange = container.read(assetSearchQueryProvider.notifier).state.getQuery();
    assert(!firstSearchAndPageChange.isEmpty());
    assert(firstSearchAndPageChange.query == "test");
    assert(firstSearchAndPageChange.page == 2);

    // Make a new search
    container.read(assetSearchQueryProvider.notifier).state.setQuery("test2");
    var secondSearch = container.read(assetSearchQueryProvider.notifier).state.getQuery();
    assert(!secondSearch.isEmpty());
    assert(secondSearch.query == "test2");
    assert(secondSearch.page == 1);

    // Change everything
    container.read(assetSearchQueryProvider.notifier).state.setQueryAndPage("test3", 3);
    var thirdSearch = container.read(assetSearchQueryProvider.notifier).state.getQuery();
    assert(!thirdSearch.isEmpty());
    assert(thirdSearch.query == "test3");
    assert(thirdSearch.page == 3);

    // Try to change to wrong page number
    expect(() => container.read(assetSearchQueryProvider.notifier).state.setPage(-1), throwsAssertionError);
    expect(() => container.read(assetSearchQueryProvider.notifier).state.setPage(0), throwsAssertionError);
  });

  test('AssetSearchQuery class behavior', () {
    // With query set to null, it won't compile:
    // The argument type 'Null' can't be assigned to the parameter type 'String'
    // SearchQuery() or SearchQuery(null);

    // Testing an empty query.
    var emptyQueryWithConstructor = AssetSearchQuery.empty();
    assert(emptyQueryWithConstructor.isEmpty());
    assert(emptyQueryWithConstructor.query == "");
    assert(emptyQueryWithConstructor.page == 1);

    // Testing an empty query.
    var emptyQuery = AssetSearchQuery("");
    assert(emptyQuery.isEmpty());
    assert(emptyQuery.query == "");
    assert(emptyQuery.page == 1);

    // Testing with a query.
    var existingQueryPage1 = AssetSearchQuery("a");
    assert(!existingQueryPage1.isEmpty());
    assert(existingQueryPage1.query == "a");
    assert(existingQueryPage1.page == 1);

    // Testing with a query.
    var existingQueryPage2 = AssetSearchQuery("a", page: 2);
    assert(!existingQueryPage2.isEmpty());
    assert(existingQueryPage2.query == "a");
    assert(existingQueryPage2.page == 2);

    // Testing queries with wrong page numbers.
    expect(() => AssetSearchQuery("test", page: -1), throwsAssertionError);
    expect(() => AssetSearchQuery("test", page: 0), throwsAssertionError);
  });
}
