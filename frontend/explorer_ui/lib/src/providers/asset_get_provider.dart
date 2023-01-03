import 'package:explorer_ui/graphql/api/assetByAssetId.data.gql.dart';
import 'package:ferry/ferry.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

import '../../graphql/api/assetByAssetId.req.gql.dart';
import '../../graphql/api/assetByAssetId.var.gql.dart';
import '../configuration/graphql.dart';

part 'asset_get_provider.g.dart';

/// This method calls the query asset.
/// FutureProvider is the equivalent of Provider but for asynchronous code.
@riverpod
Future<OperationResponse<GassetByAssetIdData, GassetByAssetIdVars>> callAssetByAssetId(CallAssetByAssetIdRef ref) async {
  // We use `ref.watch` to listen to another provider, and we pass it the provider that we want to consume.
  final assetByAssetIdQuery = ref.watch(assetByAssetIdQueryProvider);

  // We run the query with the parameters
  final request = GassetByAssetIdReq((b) => b
    ..vars.assetId = assetByAssetIdQuery.assetId);
  var client = await gqlClient();

  // TODO Treat error with ${result.hasErrors}");
  // print("--> ${result.hasErrors}");
  // print("--> ${result.linkException}");

  return client.request(request).first;
}

/// We are using StateNotifierProvider to allows the UI to interact with the AssetByAssetIdQueryNotifier
final assetByAssetIdQueryProvider = StateNotifierProvider<AssetByAssetIdQueryNotifier, AssetByAssetIdQuery>((ref) {
  return AssetByAssetIdQueryNotifier();
});

/// StateNotifier is an observable class that stores a single immutable state
/// The StateNotifier class that will be passed to our StateNotifierProvider
/// The public methods on this class will be what allow the UI to modify the state
class AssetByAssetIdQueryNotifier extends StateNotifier<AssetByAssetIdQuery> {
  AssetByAssetIdQueryNotifier() : super(AssetByAssetIdQuery.empty());

  /// Set asset id
  void setAssetId(String assetId) {
    if (assetId != state.assetId) {
      state = AssetByAssetIdQuery(assetId);
    }
  }

  /// Returns the query
  AssetByAssetIdQuery getQuery() => state;
}

/// AssetByAssetIdQuery represents the asset id we want to display
@immutable
class AssetByAssetIdQuery {
  /// Asset id size
  static const int assetIdSize = 64;

  /// Asset id
  final String assetId;

  /// Constructor for an empty query
  AssetByAssetIdQuery.empty()
      : this.assetId = "";

  /// Constructor (We check the asset id size)
  const AssetByAssetIdQuery(this.assetId)
      : assert(assetId.length == assetIdSize, "Invalid asset ID size - ${assetId.length} instead of ${assetIdSize}");

  /// Returns true if there is not query to make
  bool isEmpty() {
    return assetId.isEmpty;
  }
}
