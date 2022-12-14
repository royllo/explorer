// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/getUser.ast.gql.dart' as _i5;
import 'package:explorer_ui/graphql/api/getUser.data.gql.dart' as _i2;
import 'package:explorer_ui/graphql/api/getUser.var.gql.dart' as _i3;
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i6;
import 'package:ferry_exec/ferry_exec.dart' as _i1;
import 'package:gql_exec/gql_exec.dart' as _i4;

part 'getUser.req.gql.g.dart';

abstract class GuserByUsernameReq
    implements
        Built<GuserByUsernameReq, GuserByUsernameReqBuilder>,
        _i1.OperationRequest<_i2.GuserByUsernameData, _i3.GuserByUsernameVars> {
  GuserByUsernameReq._();

  factory GuserByUsernameReq([Function(GuserByUsernameReqBuilder b) updates]) =
      _$GuserByUsernameReq;

  static void _initializeBuilder(GuserByUsernameReqBuilder b) => b
    ..operation = _i4.Operation(
      document: _i5.document,
      operationName: 'userByUsername',
    )
    ..executeOnListen = true;
  @override
  _i3.GuserByUsernameVars get vars;
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
  _i2.GuserByUsernameData? Function(
    _i2.GuserByUsernameData?,
    _i2.GuserByUsernameData?,
  )? get updateResult;
  @override
  _i2.GuserByUsernameData? get optimisticResponse;
  @override
  String? get updateCacheHandlerKey;
  @override
  Map<String, dynamic>? get updateCacheHandlerContext;
  @override
  _i1.FetchPolicy? get fetchPolicy;
  @override
  bool get executeOnListen;
  @override
  _i2.GuserByUsernameData? parseData(Map<String, dynamic> json) =>
      _i2.GuserByUsernameData.fromJson(json);
  static Serializer<GuserByUsernameReq> get serializer =>
      _$guserByUsernameReqSerializer;
  Map<String, dynamic> toJson() => (_i6.serializers.serializeWith(
        GuserByUsernameReq.serializer,
        this,
      ) as Map<String, dynamic>);
  static GuserByUsernameReq? fromJson(Map<String, dynamic> json) =>
      _i6.serializers.deserializeWith(
        GuserByUsernameReq.serializer,
        json,
      );
}
