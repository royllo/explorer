// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i2;
import 'package:gql_code_builder/src/serializers/default_scalar_serializer.dart'
    as _i1;

part 'schema.schema.gql.g.dart';

abstract class G_FieldSet implements Built<G_FieldSet, G_FieldSetBuilder> {
  G_FieldSet._();

  factory G_FieldSet([String? value]) =>
      _$G_FieldSet((b) => value != null ? (b..value = value) : b);

  String get value;
  @BuiltValueSerializer(custom: true)
  static Serializer<G_FieldSet> get serializer =>
      _i1.DefaultScalarSerializer<G_FieldSet>(
          (Object serialized) => G_FieldSet((serialized as String?)));
}

abstract class GaddAssetMetaDataRequestInputs
    implements
        Built<GaddAssetMetaDataRequestInputs,
            GaddAssetMetaDataRequestInputsBuilder> {
  GaddAssetMetaDataRequestInputs._();

  factory GaddAssetMetaDataRequestInputs(
          [Function(GaddAssetMetaDataRequestInputsBuilder b) updates]) =
      _$GaddAssetMetaDataRequestInputs;

  String get assetId;
  String get metaData;
  static Serializer<GaddAssetMetaDataRequestInputs> get serializer =>
      _$gaddAssetMetaDataRequestInputsSerializer;
  Map<String, dynamic> toJson() => (_i2.serializers.serializeWith(
        GaddAssetMetaDataRequestInputs.serializer,
        this,
      ) as Map<String, dynamic>);
  static GaddAssetMetaDataRequestInputs? fromJson(Map<String, dynamic> json) =>
      _i2.serializers.deserializeWith(
        GaddAssetMetaDataRequestInputs.serializer,
        json,
      );
}

abstract class GAddAssetRequestInputs
    implements Built<GAddAssetRequestInputs, GAddAssetRequestInputsBuilder> {
  GAddAssetRequestInputs._();

  factory GAddAssetRequestInputs(
          [Function(GAddAssetRequestInputsBuilder b) updates]) =
      _$GAddAssetRequestInputs;

  String get genesisPoint;
  String get name;
  String get metaData;
  String get assetId;
  int get outputIndex;
  String get proof;
  static Serializer<GAddAssetRequestInputs> get serializer =>
      _$gAddAssetRequestInputsSerializer;
  Map<String, dynamic> toJson() => (_i2.serializers.serializeWith(
        GAddAssetRequestInputs.serializer,
        this,
      ) as Map<String, dynamic>);
  static GAddAssetRequestInputs? fromJson(Map<String, dynamic> json) =>
      _i2.serializers.deserializeWith(
        GAddAssetRequestInputs.serializer,
        json,
      );
}

abstract class GBigDecimal implements Built<GBigDecimal, GBigDecimalBuilder> {
  GBigDecimal._();

  factory GBigDecimal([String? value]) =>
      _$GBigDecimal((b) => value != null ? (b..value = value) : b);

  String get value;
  @BuiltValueSerializer(custom: true)
  static Serializer<GBigDecimal> get serializer =>
      _i1.DefaultScalarSerializer<GBigDecimal>(
          (Object serialized) => GBigDecimal((serialized as String?)));
}

abstract class GBigInteger implements Built<GBigInteger, GBigIntegerBuilder> {
  GBigInteger._();

  factory GBigInteger([String? value]) =>
      _$GBigInteger((b) => value != null ? (b..value = value) : b);

  String get value;
  @BuiltValueSerializer(custom: true)
  static Serializer<GBigInteger> get serializer =>
      _i1.DefaultScalarSerializer<GBigInteger>(
          (Object serialized) => GBigInteger((serialized as String?)));
}

abstract class GDateTime implements Built<GDateTime, GDateTimeBuilder> {
  GDateTime._();

  factory GDateTime([String? value]) =>
      _$GDateTime((b) => value != null ? (b..value = value) : b);

  String get value;
  @BuiltValueSerializer(custom: true)
  static Serializer<GDateTime> get serializer =>
      _i1.DefaultScalarSerializer<GDateTime>(
          (Object serialized) => GDateTime((serialized as String?)));
}

class GErrorDetail extends EnumClass {
  const GErrorDetail._(String name) : super(name);

  static const GErrorDetail UNKNOWN = _$gErrorDetailUNKNOWN;

  static const GErrorDetail FIELD_NOT_FOUND = _$gErrorDetailFIELD_NOT_FOUND;

  static const GErrorDetail INVALID_CURSOR = _$gErrorDetailINVALID_CURSOR;

  static const GErrorDetail UNIMPLEMENTED = _$gErrorDetailUNIMPLEMENTED;

  static const GErrorDetail INVALID_ARGUMENT = _$gErrorDetailINVALID_ARGUMENT;

  static const GErrorDetail DEADLINE_EXCEEDED = _$gErrorDetailDEADLINE_EXCEEDED;

  static const GErrorDetail SERVICE_ERROR = _$gErrorDetailSERVICE_ERROR;

  static const GErrorDetail THROTTLED_CPU = _$gErrorDetailTHROTTLED_CPU;

  static const GErrorDetail THROTTLED_CONCURRENCY =
      _$gErrorDetailTHROTTLED_CONCURRENCY;

  static const GErrorDetail ENHANCE_YOUR_CALM = _$gErrorDetailENHANCE_YOUR_CALM;

  static const GErrorDetail TCP_FAILURE = _$gErrorDetailTCP_FAILURE;

  static const GErrorDetail MISSING_RESOURCE = _$gErrorDetailMISSING_RESOURCE;

  static Serializer<GErrorDetail> get serializer => _$gErrorDetailSerializer;
  static BuiltSet<GErrorDetail> get values => _$gErrorDetailValues;
  static GErrorDetail valueOf(String name) => _$gErrorDetailValueOf(name);
}

class GErrorType extends EnumClass {
  const GErrorType._(String name) : super(name);

  static const GErrorType UNKNOWN = _$gErrorTypeUNKNOWN;

  static const GErrorType INTERNAL = _$gErrorTypeINTERNAL;

  static const GErrorType NOT_FOUND = _$gErrorTypeNOT_FOUND;

  static const GErrorType UNAUTHENTICATED = _$gErrorTypeUNAUTHENTICATED;

  static const GErrorType PERMISSION_DENIED = _$gErrorTypePERMISSION_DENIED;

  static const GErrorType BAD_REQUEST = _$gErrorTypeBAD_REQUEST;

  static const GErrorType UNAVAILABLE = _$gErrorTypeUNAVAILABLE;

  static const GErrorType FAILED_PRECONDITION = _$gErrorTypeFAILED_PRECONDITION;

  static Serializer<GErrorType> get serializer => _$gErrorTypeSerializer;
  static BuiltSet<GErrorType> get values => _$gErrorTypeValues;
  static GErrorType valueOf(String name) => _$gErrorTypeValueOf(name);
}

class GRequestStatus extends EnumClass {
  const GRequestStatus._(String name) : super(name);

  static const GRequestStatus OPENED = _$gRequestStatusOPENED;

  static const GRequestStatus SUCCESS = _$gRequestStatusSUCCESS;

  static const GRequestStatus FAILURE = _$gRequestStatusFAILURE;

  static Serializer<GRequestStatus> get serializer =>
      _$gRequestStatusSerializer;
  static BuiltSet<GRequestStatus> get values => _$gRequestStatusValues;
  static GRequestStatus valueOf(String name) => _$gRequestStatusValueOf(name);
}

const Map<String, Set<String>> possibleTypesMap = {
  'Request': {
    'AddAssetMetaDataRequest',
    'AddAssetRequest',
  }
};
