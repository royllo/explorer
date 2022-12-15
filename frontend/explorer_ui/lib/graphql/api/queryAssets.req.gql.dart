// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/queryAssets.ast.gql.dart' as _i5;
import 'package:explorer_ui/graphql/api/queryAssets.data.gql.dart' as _i2;
import 'package:explorer_ui/graphql/api/queryAssets.var.gql.dart' as _i3;
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i6;
import 'package:ferry_exec/ferry_exec.dart' as _i1;
import 'package:gql_exec/gql_exec.dart' as _i4;

part 'queryAssets.req.gql.g.dart';

abstract class GqueryAssetsReq
    implements
        Built<GqueryAssetsReq, GqueryAssetsReqBuilder>,
        _i1.OperationRequest<_i2.GqueryAssetsData, _i3.GqueryAssetsVars> {
  GqueryAssetsReq._();

  factory GqueryAssetsReq([Function(GqueryAssetsReqBuilder b) updates]) =
      _$GqueryAssetsReq;

  static void _initializeBuilder(GqueryAssetsReqBuilder b) => b
    ..operation = _i4.Operation(
      document: _i5.document,
      operationName: 'queryAssets',
    )
    ..executeOnListen = true;
  @override
  _i3.GqueryAssetsVars get vars;
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
  _i2.GqueryAssetsData? Function(
    _i2.GqueryAssetsData?,
    _i2.GqueryAssetsData?,
  )? get updateResult;
  @override
  _i2.GqueryAssetsData? get optimisticResponse;
  @override
  String? get updateCacheHandlerKey;
  @override
  Map<String, dynamic>? get updateCacheHandlerContext;
  @override
  _i1.FetchPolicy? get fetchPolicy;
  @override
  bool get executeOnListen;
  @override
  _i2.GqueryAssetsData? parseData(Map<String, dynamic> json) =>
      _i2.GqueryAssetsData.fromJson(json);
  static Serializer<GqueryAssetsReq> get serializer =>
      _$gqueryAssetsReqSerializer;
  Map<String, dynamic> toJson() => (_i6.serializers.serializeWith(
        GqueryAssetsReq.serializer,
        this,
      ) as Map<String, dynamic>);
  static GqueryAssetsReq? fromJson(Map<String, dynamic> json) =>
      _i6.serializers.deserializeWith(
        GqueryAssetsReq.serializer,
        json,
      );
}
