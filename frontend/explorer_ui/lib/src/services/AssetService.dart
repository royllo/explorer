import 'package:explorer_ui/graphql/api/queryAssets.data.gql.dart';
import 'package:explorer_ui/graphql/api/queryAssets.req.gql.dart';
import 'package:explorer_ui/graphql/api/queryAssets.var.gql.dart';
import 'package:ferry/ferry.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';


import '../../main.dart';
import '../configuration/graphql.dart';

part 'AssetService.g.dart';

@riverpod
Future<OperationResponse<GqueryAssetsData, GqueryAssetsVars>> assetQuery(
    AssetQueryRef ref) async {

  // We use `ref.watch` to listen to another provider, and we pass it the provider
  // that we want to consume. Here: searchedValueProvider.
  final value = ref.watch(searchedValueProvider);

  final request = GqueryAssetsReq((b) => b
    ..fetchPolicy = FetchPolicy.NetworkOnly
    ..vars.value = value
    ..vars.pageNumber = 0);
  var client = await gqlClient();
  return await client.request(request).first;
}
