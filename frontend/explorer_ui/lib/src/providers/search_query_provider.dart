import 'package:flutter_riverpod/flutter_riverpod.dart';

/// SearchQuery represents what the user is searching for and which page
class SearchQuery {
  /// User query - Searching for "coin" for example
  String query;

  /// Page number - The result page we want to get
  int pageNumber;

  /// Constructor (Page number is optional)
  SearchQuery(this.query, {this.pageNumber = 1})
      : assert(pageNumber > 0, "Page number starts at 1");

  /// Returns true if there is not query to make
  bool isEmpty() {
    return query.isEmpty;
  }
}

class SearchQueryNotifier extends StateNotifier<SearchQuery> {
  SearchQueryNotifier(super.state);
}
