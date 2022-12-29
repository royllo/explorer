import 'package:explorer_ui/src/providers/search_query_provider.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';

final searchQueryProvider = StateProvider((ref) => SearchQueryNotifier());

void main() {
  test('Search query provider', () {

    final container = ProviderContainer();

    // Initial state
    var initialState = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(initialState.isEmpty());
    assert(initialState.query == "");
    assert(initialState.pageNumber == 1);

    // Make a search
    container.read(searchQueryProvider.notifier).state.search("test");
    var firstSearch = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(!firstSearch.isEmpty());
    assert(firstSearch.query == "test");
    assert(firstSearch.pageNumber == 1);

    // Change page
    container.read(searchQueryProvider.notifier).state.changePageNumber(2);
    var firstSearchAndPageChange = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(!firstSearchAndPageChange.isEmpty());
    assert(firstSearchAndPageChange.query == "test");
    assert(firstSearchAndPageChange.pageNumber == 2);

    // Make a new search
    container.read(searchQueryProvider.notifier).state.search("test2");
    var secondSearch = container.read(searchQueryProvider.notifier).state.getSearchQuery();
    assert(!secondSearch.isEmpty());
    assert(secondSearch.query == "test2");
    assert(secondSearch.pageNumber == 1);

    // Try to change to wrong page number
    expect(() => container.read(searchQueryProvider.notifier).state.changePageNumber(-1), throwsAssertionError);
    expect(() => container.read(searchQueryProvider.notifier).state.changePageNumber(0), throwsAssertionError);
  });
}
