// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i1;

part 'assetByAssetId.data.gql.g.dart';

abstract class GassetByAssetIdData
    implements Built<GassetByAssetIdData, GassetByAssetIdDataBuilder> {
  GassetByAssetIdData._();

  factory GassetByAssetIdData(
      [Function(GassetByAssetIdDataBuilder b) updates]) = _$GassetByAssetIdData;

  static void _initializeBuilder(GassetByAssetIdDataBuilder b) =>
      b..G__typename = 'Query';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  GassetByAssetIdData_assetByAssetId? get assetByAssetId;
  static Serializer<GassetByAssetIdData> get serializer =>
      _$gassetByAssetIdDataSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GassetByAssetIdData.serializer,
        this,
      ) as Map<String, dynamic>);
  static GassetByAssetIdData? fromJson(Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GassetByAssetIdData.serializer,
        json,
      );
}

abstract class GassetByAssetIdData_assetByAssetId
    implements
        Built<GassetByAssetIdData_assetByAssetId,
            GassetByAssetIdData_assetByAssetIdBuilder> {
  GassetByAssetIdData_assetByAssetId._();

  factory GassetByAssetIdData_assetByAssetId(
          [Function(GassetByAssetIdData_assetByAssetIdBuilder b) updates]) =
      _$GassetByAssetIdData_assetByAssetId;

  static void _initializeBuilder(GassetByAssetIdData_assetByAssetIdBuilder b) =>
      b..G__typename = 'Asset';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  String? get assetId;
  GassetByAssetIdData_assetByAssetId_genesisPoint get genesisPoint;
  GassetByAssetIdData_assetByAssetId_creator get creator;
  String? get name;
  String? get metaData;
  int? get outputIndex;
  static Serializer<GassetByAssetIdData_assetByAssetId> get serializer =>
      _$gassetByAssetIdDataAssetByAssetIdSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GassetByAssetIdData_assetByAssetId.serializer,
        this,
      ) as Map<String, dynamic>);
  static GassetByAssetIdData_assetByAssetId? fromJson(
          Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GassetByAssetIdData_assetByAssetId.serializer,
        json,
      );
}

abstract class GassetByAssetIdData_assetByAssetId_genesisPoint
    implements
        Built<GassetByAssetIdData_assetByAssetId_genesisPoint,
            GassetByAssetIdData_assetByAssetId_genesisPointBuilder> {
  GassetByAssetIdData_assetByAssetId_genesisPoint._();

  factory GassetByAssetIdData_assetByAssetId_genesisPoint(
      [Function(GassetByAssetIdData_assetByAssetId_genesisPointBuilder b)
          updates]) = _$GassetByAssetIdData_assetByAssetId_genesisPoint;

  static void _initializeBuilder(
          GassetByAssetIdData_assetByAssetId_genesisPointBuilder b) =>
      b..G__typename = 'BitcoinTransactionOutput';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  int? get blockHeight;
  String get txId;
  int get vout;
  String? get scriptPubKey;
  String? get scriptPubKeyAsm;
  String? get scriptPubKeyType;
  String? get scriptPubKeyAddress;
  static Serializer<GassetByAssetIdData_assetByAssetId_genesisPoint>
      get serializer =>
          _$gassetByAssetIdDataAssetByAssetIdGenesisPointSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GassetByAssetIdData_assetByAssetId_genesisPoint.serializer,
        this,
      ) as Map<String, dynamic>);
  static GassetByAssetIdData_assetByAssetId_genesisPoint? fromJson(
          Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GassetByAssetIdData_assetByAssetId_genesisPoint.serializer,
        json,
      );
}

abstract class GassetByAssetIdData_assetByAssetId_creator
    implements
        Built<GassetByAssetIdData_assetByAssetId_creator,
            GassetByAssetIdData_assetByAssetId_creatorBuilder> {
  GassetByAssetIdData_assetByAssetId_creator._();

  factory GassetByAssetIdData_assetByAssetId_creator(
      [Function(GassetByAssetIdData_assetByAssetId_creatorBuilder b)
          updates]) = _$GassetByAssetIdData_assetByAssetId_creator;

  static void _initializeBuilder(
          GassetByAssetIdData_assetByAssetId_creatorBuilder b) =>
      b..G__typename = 'User';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  String get id;
  String get username;
  static Serializer<GassetByAssetIdData_assetByAssetId_creator>
      get serializer => _$gassetByAssetIdDataAssetByAssetIdCreatorSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GassetByAssetIdData_assetByAssetId_creator.serializer,
        this,
      ) as Map<String, dynamic>);
  static GassetByAssetIdData_assetByAssetId_creator? fromJson(
          Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GassetByAssetIdData_assetByAssetId_creator.serializer,
        json,
      );
}
