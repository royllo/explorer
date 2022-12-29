// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

import 'package:built_collection/built_collection.dart';
import 'package:built_value/serializer.dart';
import 'package:built_value/standard_json_plugin.dart' show StandardJsonPlugin;
import 'package:explorer_ui/graphql/api/assetByAssetId.data.gql.dart'
    show
        GassetByAssetIdData,
        GassetByAssetIdData_assetByAssetId,
        GassetByAssetIdData_assetByAssetId_creator,
        GassetByAssetIdData_assetByAssetId_genesisPoint;
import 'package:explorer_ui/graphql/api/assetByAssetId.req.gql.dart'
    show GassetByAssetIdReq;
import 'package:explorer_ui/graphql/api/assetByAssetId.var.gql.dart'
    show GassetByAssetIdVars;
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
  GassetByAssetIdData,
  GassetByAssetIdData_assetByAssetId,
  GassetByAssetIdData_assetByAssetId_creator,
  GassetByAssetIdData_assetByAssetId_genesisPoint,
  GassetByAssetIdReq,
  GassetByAssetIdVars,
  GqueryAssetsData,
  GqueryAssetsData_queryAssets,
  GqueryAssetsData_queryAssets_content,
  GqueryAssetsReq,
  GqueryAssetsVars,
])
final Serializers serializers = _serializersBuilder.build();
