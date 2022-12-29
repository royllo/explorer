// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/assetByAssetId.ast.gql.dart' as _i5;
import 'package:explorer_ui/graphql/api/assetByAssetId.data.gql.dart' as _i2;
import 'package:explorer_ui/graphql/api/assetByAssetId.var.gql.dart' as _i3;
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i6;
import 'package:ferry_exec/ferry_exec.dart' as _i1;
import 'package:gql_exec/gql_exec.dart' as _i4;

part 'assetByAssetId.req.gql.g.dart';

abstract class GassetByAssetIdReq
    implements
        Built<GassetByAssetIdReq, GassetByAssetIdReqBuilder>,
        _i1.OperationRequest<_i2.GassetByAssetIdData, _i3.GassetByAssetIdVars> {
  GassetByAssetIdReq._();

  factory GassetByAssetIdReq([Function(GassetByAssetIdReqBuilder b) updates]) =
      _$GassetByAssetIdReq;

  static void _initializeBuilder(GassetByAssetIdReqBuilder b) => b
    ..operation = _i4.Operation(
      document: _i5.document,
      operationName: 'assetByAssetId',
    )
    ..executeOnListen = true;
  @override
  _i3.GassetByAssetIdVars get vars;
  @override
  _i4.Operation get operation;
  @override
  _i4.Request get execRequest => _i4.Request(
        operation: operation,
        variables: vars.toJson(),
      );
  @override
  String? get requestId;
  @override
  @BuiltValueField(serialize: false)
  _i2.GassetByAssetIdData? Function(
    _i2.GassetByAssetIdData?,
    _i2.GassetByAssetIdData?,
  )? get updateResult;
  @override
  _i2.GassetByAssetIdData? get optimisticResponse;
  @override
  String? get updateCacheHandlerKey;
  @override
  Map<String, dynamic>? get updateCacheHandlerContext;
  @override
  _i1.FetchPolicy? get fetchPolicy;
  @override
  bool get executeOnListen;
  @override
  _i2.GassetByAssetIdData? parseData(Map<String, dynamic> json) =>
      _i2.GassetByAssetIdData.fromJson(json);
  static Serializer<GassetByAssetIdReq> get serializer =>
      _$gassetByAssetIdReqSerializer;
  Map<String, dynamic> toJson() => (_i6.serializers.serializeWith(
        GassetByAssetIdReq.serializer,
        this,
      ) as Map<String, dynamic>);
  static GassetByAssetIdReq? fromJson(Map<String, dynamic> json) =>
      _i6.serializers.deserializeWith(
        GassetByAssetIdReq.serializer,
        json,
      );
}
