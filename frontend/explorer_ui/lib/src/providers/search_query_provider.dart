import 'package:flutter/cupertino.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

/// SearchQuery represents what the user is searching for and which page
@immutable
class SearchQuery {
  /// User query - Searching for "coin" for example
  final String query;

  /// Page number - The result page we want to get
  final int pageNumber;

  /// Constructor (Page number is optional but starts at 1)
  const SearchQuery(this.query, {this.pageNumber = 1})
      : assert(pageNumber >= 1, "Page number starts at 1");

  /// Returns true if there is not query to make
  bool isEmpty() {
    return query.isEmpty;
  }
}

/// StateNotifier is an observable class that stores a single immutable state
/// The StateNotifier class that will be passed to our StateNotifierProvider
/// The public methods on this class will be what allow the UI to modify the state
class SearchQueryNotifier extends StateNotifier<SearchQuery> {
  /// Constructor
  SearchQueryNotifier() : super(const SearchQuery(""));

  /// Change user query
  void search(String query) {
    state = SearchQuery(query);
  }

  /// Change page number to be displayed
  void changePageNumber(int pageNumber) {
    state = SearchQuery(state.query, pageNumber: pageNumber);
  }

  /// Returns the search query
  SearchQuery getSearchQuery() => state;
}

/// We are using StateNotifierProvider to allow the UI to interact with SearchQuery
final searchQueryProvider = StateNotifierProvider<SearchQueryNotifier, SearchQuery>((ref) {
  return SearchQueryNotifier();
});
