import 'package:flutter/cupertino.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

/// We are using StateNotifierProvider to allow the UI to interact with SearchQuery
final searchQueryProvider = StateNotifierProvider<SearchQueryNotifier, SearchQuery>((ref) {
  return SearchQueryNotifier();
});

/// StateNotifier is an observable class that stores a single immutable state
/// The StateNotifier class that will be passed to our StateNotifierProvider
/// The public methods on this class will be what allow the UI to modify the state
class SearchQueryNotifier extends StateNotifier<SearchQuery> {
  /// Constructor
  SearchQueryNotifier() : super(SearchQuery.empty());

  /// Set query and page
  void setQueryAndPage(String query, int page) {
    if (query != state.query || page != state.page) {
      state = SearchQuery(query, page: page);
    }
  }

  /// Set user query
  void setQuery(String query) {
    if (query != state.query) {
      state = SearchQuery(query);
    }
  }

  /// Set page number
  void setPage(int page) {
    if (page != state.page) {
      state = SearchQuery(state.query, page: page);
    }
  }

  /// Returns the search query
  SearchQuery getSearchQuery() => state;
}

/// SearchQuery represents what the user is searching for and which page
@immutable
class SearchQuery {
  /// First page
  static const int firstPage = 1;

  /// User query - Searching for "coin" for example
  final String query;

  /// Page number - The result page we want
  final int page;

  /// Constructor for an empty query
  SearchQuery.empty()
      : this.query = "",
        this.page = firstPage;

  /// Constructor (Page number is optional and first one is defined by "firstPage")
  const SearchQuery(this.query, {this.page = firstPage}) : assert(page >= firstPage, "Page number starts at ${firstPage}");

  /// Returns true if there is not query to make
  bool isEmpty() {
    return query.isEmpty;
  }
}
