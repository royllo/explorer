import 'package:explorer_ui/src/configuration/graphql.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:explorer_ui/graphql/api/queryAssets.data.gql.dart';
import 'package:explorer_ui/graphql/api/queryAssets.req.gql.dart';
import 'package:explorer_ui/graphql/api/queryAssets.var.gql.dart';
import 'package:ferry/ferry.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

part 'asset_search_provider.g.dart';

// asset_search_provider description
// - Method callQueryAssets waits for change in assetSearchQueryProvider and calls the GraphQL API
// - Variable assetSearchQueryProvider allows the UI to interact with AssetSearchQuery thanks to AssetSearchQueryNotifier
// - Class AssetSearchQueryNotifier is an observable class that stores a single immutable state and manipulation values
// - Class AssetSearchQuery represents what the user is searching for and which page to display

/// FutureProvider is the equivalent of Provider but for asynchronous code.
@riverpod
Future<OperationResponse<GqueryAssetsData, GqueryAssetsVars>> callQueryAssets(CallQueryAssetsRef ref) async {
  // We use `ref.watch` to listen to another provider, and we pass it the provider that we want to consume.
  final searchQuery = ref.watch(assetSearchQueryProvider);

  // We run the query with the parameters
  final request = GqueryAssetsReq((b) => b
    ..vars.value = searchQuery.query
    ..vars.pageNumber = searchQuery.page);
  var client = await gqlClient();
  return client.request(request).first;
}

  /// We are using StateNotifierProvider to allows the UI to interact with the AssetSearchQueryNotifier
final assetSearchQueryProvider = StateNotifierProvider<AssetSearchQueryNotifier, AssetSearchQuery>((ref) {
  return AssetSearchQueryNotifier();
});

/// StateNotifier is an observable class that stores a single immutable state
/// The StateNotifier class that will be passed to our StateNotifierProvider
/// The public methods on this class will be what allow the UI to modify the state
class AssetSearchQueryNotifier extends StateNotifier<AssetSearchQuery> {
  /// Constructor
  AssetSearchQueryNotifier() : super(AssetSearchQuery.empty());

  /// Set query and page
  void setQueryAndPage(String query, int page) {
    if (query != state.query || page != state.page) {
      state = AssetSearchQuery(query, page: page);
    }
  }

  /// Set user query
  void setQuery(String query) {
    if (query != state.query) {
      state = AssetSearchQuery(query);
    }
  }

  /// Set page number
  void setPage(int page) {
    if (page != state.page) {
      state = AssetSearchQuery(state.query, page: page);
    }
  }

  /// Returns the search query
  AssetSearchQuery getSearchQuery() => state;
}

/// AssetSearchQuery represents what the user is searching for and which page to display
@immutable
class AssetSearchQuery {
  /// First page
  static const int firstPage = 1;

  /// User query - Searching for "coin" for example
  final String query;

  /// Page number - The result page we want
  final int page;

  /// Constructor for an empty query
  AssetSearchQuery.empty()
      : this.query = "",
        this.page = firstPage;

  /// Constructor (Page number is optional and first one is defined by "firstPage")
  const AssetSearchQuery(this.query, {this.page = firstPage}) : assert(page >= firstPage, "Page number starts at ${firstPage}");

  /// Returns true if there is not query to make
  bool isEmpty() {
    return query.isEmpty;
  }
}
