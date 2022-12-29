// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i1;

part 'assetByAssetId.var.gql.g.dart';

abstract class GassetByAssetIdVars
    implements Built<GassetByAssetIdVars, GassetByAssetIdVarsBuilder> {
  GassetByAssetIdVars._();

  factory GassetByAssetIdVars(
      [Function(GassetByAssetIdVarsBuilder b) updates]) = _$GassetByAssetIdVars;

  String get assetId;
  static Serializer<GassetByAssetIdVars> get serializer =>
      _$gassetByAssetIdVarsSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GassetByAssetIdVars.serializer,
        this,
      ) as Map<String, dynamic>);
  static GassetByAssetIdVars? fromJson(Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GassetByAssetIdVars.serializer,
        json,
      );
}
