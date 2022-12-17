// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i1;

part 'queryAssets.data.gql.g.dart';

abstract class GqueryAssetsData
    implements Built<GqueryAssetsData, GqueryAssetsDataBuilder> {
  GqueryAssetsData._();

  factory GqueryAssetsData([Function(GqueryAssetsDataBuilder b) updates]) =
      _$GqueryAssetsData;

  static void _initializeBuilder(GqueryAssetsDataBuilder b) =>
      b..G__typename = 'Query';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  GqueryAssetsData_queryAssets? get queryAssets;
  static Serializer<GqueryAssetsData> get serializer =>
      _$gqueryAssetsDataSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GqueryAssetsData.serializer,
        this,
      ) as Map<String, dynamic>);
  static GqueryAssetsData? fromJson(Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GqueryAssetsData.serializer,
        json,
      );
}

abstract class GqueryAssetsData_queryAssets
    implements
        Built<GqueryAssetsData_queryAssets,
            GqueryAssetsData_queryAssetsBuilder> {
  GqueryAssetsData_queryAssets._();

  factory GqueryAssetsData_queryAssets(
          [Function(GqueryAssetsData_queryAssetsBuilder b) updates]) =
      _$GqueryAssetsData_queryAssets;

  static void _initializeBuilder(GqueryAssetsData_queryAssetsBuilder b) =>
      b..G__typename = 'AssetPage';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  BuiltList<GqueryAssetsData_queryAssets_content?>? get content;
  int get totalPages;
  static Serializer<GqueryAssetsData_queryAssets> get serializer =>
      _$gqueryAssetsDataQueryAssetsSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GqueryAssetsData_queryAssets.serializer,
        this,
      ) as Map<String, dynamic>);
  static GqueryAssetsData_queryAssets? fromJson(Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GqueryAssetsData_queryAssets.serializer,
        json,
      );
}

abstract class GqueryAssetsData_queryAssets_content
    implements
        Built<GqueryAssetsData_queryAssets_content,
            GqueryAssetsData_queryAssets_contentBuilder> {
  GqueryAssetsData_queryAssets_content._();

  factory GqueryAssetsData_queryAssets_content(
          [Function(GqueryAssetsData_queryAssets_contentBuilder b) updates]) =
      _$GqueryAssetsData_queryAssets_content;

  static void _initializeBuilder(
          GqueryAssetsData_queryAssets_contentBuilder b) =>
      b..G__typename = 'Asset';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  String? get assetId;
  String? get name;
  static Serializer<GqueryAssetsData_queryAssets_content> get serializer =>
      _$gqueryAssetsDataQueryAssetsContentSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GqueryAssetsData_queryAssets_content.serializer,
        this,
      ) as Map<String, dynamic>);
  static GqueryAssetsData_queryAssets_content? fromJson(
          Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GqueryAssetsData_queryAssets_content.serializer,
        json,
      );
}
