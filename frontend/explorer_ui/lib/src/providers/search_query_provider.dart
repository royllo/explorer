import 'package:explorer_ui/src/configuration/graphql.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:explorer_ui/graphql/api/queryAssets.data.gql.dart';
import 'package:explorer_ui/graphql/api/queryAssets.req.gql.dart';
import 'package:explorer_ui/graphql/api/queryAssets.var.gql.dart';
import 'package:ferry/ferry.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part 'search_query_provider.g.dart';

@riverpod
/// FutureProvider is the equivalent of Provider but for asynchronous code.
Future<OperationResponse<GqueryAssetsData, GqueryAssetsVars>> assetQuery(AssetQueryRef ref) async {
  // We use `ref.watch` to listen to another provider, and we pass it the provider that we want to consume.
  final searchQuery = ref.watch(searchQueryProvider);

  // We run the query with the parameters
  final request = GqueryAssetsReq((b) => b
    ..vars.value = searchQuery.query
    ..vars.pageNumber = searchQuery.page);
  var client = await gqlClient();
  // TODO I think this await should be removed no ?
  return await client.request(request).first;
}

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
