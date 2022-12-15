// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

import 'package:built_collection/built_collection.dart';
import 'package:built_value/serializer.dart';
import 'package:built_value/standard_json_plugin.dart' show StandardJsonPlugin;
import 'package:explorer_ui/graphql/api/getUser.data.gql.dart'
    show GuserByUsernameData, GuserByUsernameData_userByUsername;
import 'package:explorer_ui/graphql/api/getUser.req.gql.dart'
    show GuserByUsernameReq;
import 'package:explorer_ui/graphql/api/getUser.var.gql.dart'
    show GuserByUsernameVars;
import 'package:explorer_ui/graphql/api/queryAssets.data.gql.dart'
    show
        GqueryAssetsData,
        GqueryAssetsData_queryAssets,
        GqueryAssetsData_queryAssets_content;
import 'package:explorer_ui/graphql/api/queryAssets.req.gql.dart'
    show GqueryAssetsReq;
import 'package:explorer_ui/graphql/api/queryAssets.var.gql.dart'
    show GqueryAssetsVars;
import 'package:explorer_ui/graphql/api/schema.schema.gql.dart'
    show
        GAddAssetRequestInputs,
        GBigDecimal,
        GBigInteger,
        GDateTime,
        GErrorDetail,
        GErrorType,
        GRequestStatus,
        G_FieldSet,
        GaddAssetMetaDataRequestInputs;
import 'package:ferry_exec/ferry_exec.dart';
import 'package:gql_code_builder/src/serializers/operation_serializer.dart'
    show OperationSerializer;

part 'serializers.gql.g.dart';

final SerializersBuilder _serializersBuilder = _$serializers.toBuilder()
  ..add(OperationSerializer())
  ..addPlugin(StandardJsonPlugin());
@SerializersFor([
  GAddAssetRequestInputs,
  GBigDecimal,
  GBigInteger,
  GDateTime,
  GErrorDetail,
  GErrorType,
  GRequestStatus,
  G_FieldSet,
  GaddAssetMetaDataRequestInputs,
  GqueryAssetsData,
  GqueryAssetsData_queryAssets,
  GqueryAssetsData_queryAssets_content,
  GqueryAssetsReq,
  GqueryAssetsVars,
  GuserByUsernameData,
  GuserByUsernameData_userByUsername,
  GuserByUsernameReq,
  GuserByUsernameVars,
])
final Serializers serializers = _serializersBuilder.build();
