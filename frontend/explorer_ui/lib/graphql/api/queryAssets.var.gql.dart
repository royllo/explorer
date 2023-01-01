// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i1;

part 'queryAssets.var.gql.g.dart';

abstract class GqueryAssetsVars
    implements Built<GqueryAssetsVars, GqueryAssetsVarsBuilder> {
  GqueryAssetsVars._();

  factory GqueryAssetsVars([Function(GqueryAssetsVarsBuilder b) updates]) =
      _$GqueryAssetsVars;

  String get query;
  int? get page;
  static Serializer<GqueryAssetsVars> get serializer =>
      _$gqueryAssetsVarsSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GqueryAssetsVars.serializer,
        this,
      ) as Map<String, dynamic>);
  static GqueryAssetsVars? fromJson(Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GqueryAssetsVars.serializer,
        json,
      );
}
