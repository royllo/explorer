// User service.
import 'dart:developer';

import 'package:ferry/ferry.dart';
import 'package:gql_http_link/gql_http_link.dart';
import 'package:riverpod_annotation/riverpod_annotation.dart';

import '../../graphql/api/getUser.data.gql.dart';
import '../../graphql/api/getUser.req.gql.dart';
import '../../graphql/api/getUser.var.gql.dart';
import '../configuration/graphql.dart';

// Includes riverpod generated classes.
part 'UserService.g.dart';

@riverpod
Future<OperationResponse<GuserByUsernameData, GuserByUsernameVars>> getUser(GetUserRef ref) async {
  final request = GuserByUsernameReq((b) => b
    ..fetchPolicy = FetchPolicy.NetworkOnly
    ..vars.username = "anonymous");
  //var client = await gqlClient();
  final link = HttpLink("http://localhost:9090/graphql");
  final client = Client(link: link);

  print('ici');
  final result = await client.request(request).first;
  print(result.linkException);
  return result;
}
