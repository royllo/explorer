// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'schema.schema.gql.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

const GErrorDetail _$gErrorDetailUNKNOWN = const GErrorDetail._('UNKNOWN');
const GErrorDetail _$gErrorDetailFIELD_NOT_FOUND =
    const GErrorDetail._('FIELD_NOT_FOUND');
const GErrorDetail _$gErrorDetailINVALID_CURSOR =
    const GErrorDetail._('INVALID_CURSOR');
const GErrorDetail _$gErrorDetailUNIMPLEMENTED =
    const GErrorDetail._('UNIMPLEMENTED');
const GErrorDetail _$gErrorDetailINVALID_ARGUMENT =
    const GErrorDetail._('INVALID_ARGUMENT');
const GErrorDetail _$gErrorDetailDEADLINE_EXCEEDED =
    const GErrorDetail._('DEADLINE_EXCEEDED');
const GErrorDetail _$gErrorDetailSERVICE_ERROR =
    const GErrorDetail._('SERVICE_ERROR');
const GErrorDetail _$gErrorDetailTHROTTLED_CPU =
    const GErrorDetail._('THROTTLED_CPU');
const GErrorDetail _$gErrorDetailTHROTTLED_CONCURRENCY =
    const GErrorDetail._('THROTTLED_CONCURRENCY');
const GErrorDetail _$gErrorDetailENHANCE_YOUR_CALM =
    const GErrorDetail._('ENHANCE_YOUR_CALM');
const GErrorDetail _$gErrorDetailTCP_FAILURE =
    const GErrorDetail._('TCP_FAILURE');
const GErrorDetail _$gErrorDetailMISSING_RESOURCE =
    const GErrorDetail._('MISSING_RESOURCE');

GErrorDetail _$gErrorDetailValueOf(String name) {
  switch (name) {
    case 'UNKNOWN':
      return _$gErrorDetailUNKNOWN;
    case 'FIELD_NOT_FOUND':
      return _$gErrorDetailFIELD_NOT_FOUND;
    case 'INVALID_CURSOR':
      return _$gErrorDetailINVALID_CURSOR;
    case 'UNIMPLEMENTED':
      return _$gErrorDetailUNIMPLEMENTED;
    case 'INVALID_ARGUMENT':
      return _$gErrorDetailINVALID_ARGUMENT;
    case 'DEADLINE_EXCEEDED':
      return _$gErrorDetailDEADLINE_EXCEEDED;
    case 'SERVICE_ERROR':
      return _$gErrorDetailSERVICE_ERROR;
    case 'THROTTLED_CPU':
      return _$gErrorDetailTHROTTLED_CPU;
    case 'THROTTLED_CONCURRENCY':
      return _$gErrorDetailTHROTTLED_CONCURRENCY;
    case 'ENHANCE_YOUR_CALM':
      return _$gErrorDetailENHANCE_YOUR_CALM;
    case 'TCP_FAILURE':
      return _$gErrorDetailTCP_FAILURE;
    case 'MISSING_RESOURCE':
      return _$gErrorDetailMISSING_RESOURCE;
    default:
      throw new ArgumentError(name);
  }
}

final BuiltSet<GErrorDetail> _$gErrorDetailValues =
    new BuiltSet<GErrorDetail>(const <GErrorDetail>[
  _$gErrorDetailUNKNOWN,
  _$gErrorDetailFIELD_NOT_FOUND,
  _$gErrorDetailINVALID_CURSOR,
  _$gErrorDetailUNIMPLEMENTED,
  _$gErrorDetailINVALID_ARGUMENT,
  _$gErrorDetailDEADLINE_EXCEEDED,
  _$gErrorDetailSERVICE_ERROR,
  _$gErrorDetailTHROTTLED_CPU,
  _$gErrorDetailTHROTTLED_CONCURRENCY,
  _$gErrorDetailENHANCE_YOUR_CALM,
  _$gErrorDetailTCP_FAILURE,
  _$gErrorDetailMISSING_RESOURCE,
]);

const GErrorType _$gErrorTypeUNKNOWN = const GErrorType._('UNKNOWN');
const GErrorType _$gErrorTypeINTERNAL = const GErrorType._('INTERNAL');
const GErrorType _$gErrorTypeNOT_FOUND = const GErrorType._('NOT_FOUND');
const GErrorType _$gErrorTypeUNAUTHENTICATED =
    const GErrorType._('UNAUTHENTICATED');
const GErrorType _$gErrorTypePERMISSION_DENIED =
    const GErrorType._('PERMISSION_DENIED');
const GErrorType _$gErrorTypeBAD_REQUEST = const GErrorType._('BAD_REQUEST');
const GErrorType _$gErrorTypeUNAVAILABLE = const GErrorType._('UNAVAILABLE');
const GErrorType _$gErrorTypeFAILED_PRECONDITION =
    const GErrorType._('FAILED_PRECONDITION');

GErrorType _$gErrorTypeValueOf(String name) {
  switch (name) {
    case 'UNKNOWN':
      return _$gErrorTypeUNKNOWN;
    case 'INTERNAL':
      return _$gErrorTypeINTERNAL;
    case 'NOT_FOUND':
      return _$gErrorTypeNOT_FOUND;
    case 'UNAUTHENTICATED':
      return _$gErrorTypeUNAUTHENTICATED;
    case 'PERMISSION_DENIED':
      return _$gErrorTypePERMISSION_DENIED;
    case 'BAD_REQUEST':
      return _$gErrorTypeBAD_REQUEST;
    case 'UNAVAILABLE':
      return _$gErrorTypeUNAVAILABLE;
    case 'FAILED_PRECONDITION':
      return _$gErrorTypeFAILED_PRECONDITION;
    default:
      throw new ArgumentError(name);
  }
}

final BuiltSet<GErrorType> _$gErrorTypeValues =
    new BuiltSet<GErrorType>(const <GErrorType>[
  _$gErrorTypeUNKNOWN,
  _$gErrorTypeINTERNAL,
  _$gErrorTypeNOT_FOUND,
  _$gErrorTypeUNAUTHENTICATED,
  _$gErrorTypePERMISSION_DENIED,
  _$gErrorTypeBAD_REQUEST,
  _$gErrorTypeUNAVAILABLE,
  _$gErrorTypeFAILED_PRECONDITION,
]);

const GRequestStatus _$gRequestStatusOPENED = const GRequestStatus._('OPENED');
const GRequestStatus _$gRequestStatusSUCCESS =
    const GRequestStatus._('SUCCESS');
const GRequestStatus _$gRequestStatusFAILURE =
    const GRequestStatus._('FAILURE');

GRequestStatus _$gRequestStatusValueOf(String name) {
  switch (name) {
    case 'OPENED':
      return _$gRequestStatusOPENED;
    case 'SUCCESS':
      return _$gRequestStatusSUCCESS;
    case 'FAILURE':
      return _$gRequestStatusFAILURE;
    default:
      throw new ArgumentError(name);
  }
}

final BuiltSet<GRequestStatus> _$gRequestStatusValues =
    new BuiltSet<GRequestStatus>(const <GRequestStatus>[
  _$gRequestStatusOPENED,
  _$gRequestStatusSUCCESS,
  _$gRequestStatusFAILURE,
]);

Serializer<GaddAssetMetaDataRequestInputs>
    _$gaddAssetMetaDataRequestInputsSerializer =
    new _$GaddAssetMetaDataRequestInputsSerializer();
Serializer<GAddAssetRequestInputs> _$gAddAssetRequestInputsSerializer =
    new _$GAddAssetRequestInputsSerializer();
Serializer<GErrorDetail> _$gErrorDetailSerializer =
    new _$GErrorDetailSerializer();
Serializer<GErrorType> _$gErrorTypeSerializer = new _$GErrorTypeSerializer();
Serializer<GRequestStatus> _$gRequestStatusSerializer =
    new _$GRequestStatusSerializer();

class _$GaddAssetMetaDataRequestInputsSerializer
    implements StructuredSerializer<GaddAssetMetaDataRequestInputs> {
  @override
  final Iterable<Type> types = const [
    GaddAssetMetaDataRequestInputs,
    _$GaddAssetMetaDataRequestInputs
  ];
  @override
  final String wireName = 'GaddAssetMetaDataRequestInputs';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GaddAssetMetaDataRequestInputs object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      'assetId',
      serializers.serialize(object.assetId,
          specifiedType: const FullType(String)),
      'metaData',
      serializers.serialize(object.metaData,
          specifiedType: const FullType(String)),
    ];

    return result;
  }

  @override
  GaddAssetMetaDataRequestInputs deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GaddAssetMetaDataRequestInputsBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case 'assetId':
          result.assetId = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'metaData':
          result.metaData = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
      }
    }

    return result.build();
  }
}

class _$GAddAssetRequestInputsSerializer
    implements StructuredSerializer<GAddAssetRequestInputs> {
  @override
  final Iterable<Type> types = const [
    GAddAssetRequestInputs,
    _$GAddAssetRequestInputs
  ];
  @override
  final String wireName = 'GAddAssetRequestInputs';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GAddAssetRequestInputs object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      'genesisPoint',
      serializers.serialize(object.genesisPoint,
          specifiedType: const FullType(String)),
      'name',
      serializers.serialize(object.name, specifiedType: const FullType(String)),
      'metaData',
      serializers.serialize(object.metaData,
          specifiedType: const FullType(String)),
      'assetId',
      serializers.serialize(object.assetId,
          specifiedType: const FullType(String)),
      'outputIndex',
      serializers.serialize(object.outputIndex,
          specifiedType: const FullType(int)),
      'proof',
      serializers.serialize(object.proof,
          specifiedType: const FullType(String)),
    ];

    return result;
  }

  @override
  GAddAssetRequestInputs deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GAddAssetRequestInputsBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case 'genesisPoint':
          result.genesisPoint = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'name':
          result.name = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'metaData':
          result.metaData = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'assetId':
          result.assetId = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'outputIndex':
          result.outputIndex = serializers.deserialize(value,
              specifiedType: const FullType(int))! as int;
          break;
        case 'proof':
          result.proof = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
      }
    }

    return result.build();
  }
}

class _$GErrorDetailSerializer implements PrimitiveSerializer<GErrorDetail> {
  @override
  final Iterable<Type> types = const <Type>[GErrorDetail];
  @override
  final String wireName = 'GErrorDetail';

  @override
  Object serialize(Serializers serializers, GErrorDetail object,
          {FullType specifiedType = FullType.unspecified}) =>
      object.name;

  @override
  GErrorDetail deserialize(Serializers serializers, Object serialized,
          {FullType specifiedType = FullType.unspecified}) =>
      GErrorDetail.valueOf(serialized as String);
}

class _$GErrorTypeSerializer implements PrimitiveSerializer<GErrorType> {
  @override
  final Iterable<Type> types = const <Type>[GErrorType];
  @override
  final String wireName = 'GErrorType';

  @override
  Object serialize(Serializers serializers, GErrorType object,
          {FullType specifiedType = FullType.unspecified}) =>
      object.name;

  @override
  GErrorType deserialize(Serializers serializers, Object serialized,
          {FullType specifiedType = FullType.unspecified}) =>
      GErrorType.valueOf(serialized as String);
}

class _$GRequestStatusSerializer
    implements PrimitiveSerializer<GRequestStatus> {
  @override
  final Iterable<Type> types = const <Type>[GRequestStatus];
  @override
  final String wireName = 'GRequestStatus';

  @override
  Object serialize(Serializers serializers, GRequestStatus object,
          {FullType specifiedType = FullType.unspecified}) =>
      object.name;

  @override
  GRequestStatus deserialize(Serializers serializers, Object serialized,
          {FullType specifiedType = FullType.unspecified}) =>
      GRequestStatus.valueOf(serialized as String);
}

class _$G_FieldSet extends G_FieldSet {
  @override
  final String value;

  factory _$G_FieldSet([void Function(G_FieldSetBuilder)? updates]) =>
      (new G_FieldSetBuilder()..update(updates))._build();

  _$G_FieldSet._({required this.value}) : super._() {
    BuiltValueNullFieldError.checkNotNull(value, r'G_FieldSet', 'value');
  }

  @override
  G_FieldSet rebuild(void Function(G_FieldSetBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  G_FieldSetBuilder toBuilder() => new G_FieldSetBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is G_FieldSet && value == other.value;
  }

  @override
  int get hashCode {
    return $jf($jc(0, value.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'G_FieldSet')..add('value', value))
        .toString();
  }
}

class G_FieldSetBuilder implements Builder<G_FieldSet, G_FieldSetBuilder> {
  _$G_FieldSet? _$v;

  String? _value;
  String? get value => _$this._value;
  set value(String? value) => _$this._value = value;

  G_FieldSetBuilder();

  G_FieldSetBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _value = $v.value;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(G_FieldSet other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$G_FieldSet;
  }

  @override
  void update(void Function(G_FieldSetBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  G_FieldSet build() => _build();

  _$G_FieldSet _build() {
    final _$result = _$v ??
        new _$G_FieldSet._(
            value: BuiltValueNullFieldError.checkNotNull(
                value, r'G_FieldSet', 'value'));
    replace(_$result);
    return _$result;
  }
}

class _$GaddAssetMetaDataRequestInputs extends GaddAssetMetaDataRequestInputs {
  @override
  final String assetId;
  @override
  final String metaData;

  factory _$GaddAssetMetaDataRequestInputs(
          [void Function(GaddAssetMetaDataRequestInputsBuilder)? updates]) =>
      (new GaddAssetMetaDataRequestInputsBuilder()..update(updates))._build();

  _$GaddAssetMetaDataRequestInputs._(
      {required this.assetId, required this.metaData})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        assetId, r'GaddAssetMetaDataRequestInputs', 'assetId');
    BuiltValueNullFieldError.checkNotNull(
        metaData, r'GaddAssetMetaDataRequestInputs', 'metaData');
  }

  @override
  GaddAssetMetaDataRequestInputs rebuild(
          void Function(GaddAssetMetaDataRequestInputsBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GaddAssetMetaDataRequestInputsBuilder toBuilder() =>
      new GaddAssetMetaDataRequestInputsBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GaddAssetMetaDataRequestInputs &&
        assetId == other.assetId &&
        metaData == other.metaData;
  }

  @override
  int get hashCode {
    return $jf($jc($jc(0, assetId.hashCode), metaData.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GaddAssetMetaDataRequestInputs')
          ..add('assetId', assetId)
          ..add('metaData', metaData))
        .toString();
  }
}

class GaddAssetMetaDataRequestInputsBuilder
    implements
        Builder<GaddAssetMetaDataRequestInputs,
            GaddAssetMetaDataRequestInputsBuilder> {
  _$GaddAssetMetaDataRequestInputs? _$v;

  String? _assetId;
  String? get assetId => _$this._assetId;
  set assetId(String? assetId) => _$this._assetId = assetId;

  String? _metaData;
  String? get metaData => _$this._metaData;
  set metaData(String? metaData) => _$this._metaData = metaData;

  GaddAssetMetaDataRequestInputsBuilder();

  GaddAssetMetaDataRequestInputsBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _assetId = $v.assetId;
      _metaData = $v.metaData;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GaddAssetMetaDataRequestInputs other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GaddAssetMetaDataRequestInputs;
  }

  @override
  void update(void Function(GaddAssetMetaDataRequestInputsBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GaddAssetMetaDataRequestInputs build() => _build();

  _$GaddAssetMetaDataRequestInputs _build() {
    final _$result = _$v ??
        new _$GaddAssetMetaDataRequestInputs._(
            assetId: BuiltValueNullFieldError.checkNotNull(
                assetId, r'GaddAssetMetaDataRequestInputs', 'assetId'),
            metaData: BuiltValueNullFieldError.checkNotNull(
                metaData, r'GaddAssetMetaDataRequestInputs', 'metaData'));
    replace(_$result);
    return _$result;
  }
}

class _$GAddAssetRequestInputs extends GAddAssetRequestInputs {
  @override
  final String genesisPoint;
  @override
  final String name;
  @override
  final String metaData;
  @override
  final String assetId;
  @override
  final int outputIndex;
  @override
  final String proof;

  factory _$GAddAssetRequestInputs(
          [void Function(GAddAssetRequestInputsBuilder)? updates]) =>
      (new GAddAssetRequestInputsBuilder()..update(updates))._build();

  _$GAddAssetRequestInputs._(
      {required this.genesisPoint,
      required this.name,
      required this.metaData,
      required this.assetId,
      required this.outputIndex,
      required this.proof})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        genesisPoint, r'GAddAssetRequestInputs', 'genesisPoint');
    BuiltValueNullFieldError.checkNotNull(
        name, r'GAddAssetRequestInputs', 'name');
    BuiltValueNullFieldError.checkNotNull(
        metaData, r'GAddAssetRequestInputs', 'metaData');
    BuiltValueNullFieldError.checkNotNull(
        assetId, r'GAddAssetRequestInputs', 'assetId');
    BuiltValueNullFieldError.checkNotNull(
        outputIndex, r'GAddAssetRequestInputs', 'outputIndex');
    BuiltValueNullFieldError.checkNotNull(
        proof, r'GAddAssetRequestInputs', 'proof');
  }

  @override
  GAddAssetRequestInputs rebuild(
          void Function(GAddAssetRequestInputsBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GAddAssetRequestInputsBuilder toBuilder() =>
      new GAddAssetRequestInputsBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GAddAssetRequestInputs &&
        genesisPoint == other.genesisPoint &&
        name == other.name &&
        metaData == other.metaData &&
        assetId == other.assetId &&
        outputIndex == other.outputIndex &&
        proof == other.proof;
  }

  @override
  int get hashCode {
    return $jf($jc(
        $jc(
            $jc(
                $jc($jc($jc(0, genesisPoint.hashCode), name.hashCode),
                    metaData.hashCode),
                assetId.hashCode),
            outputIndex.hashCode),
        proof.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GAddAssetRequestInputs')
          ..add('genesisPoint', genesisPoint)
          ..add('name', name)
          ..add('metaData', metaData)
          ..add('assetId', assetId)
          ..add('outputIndex', outputIndex)
          ..add('proof', proof))
        .toString();
  }
}

class GAddAssetRequestInputsBuilder
    implements Builder<GAddAssetRequestInputs, GAddAssetRequestInputsBuilder> {
  _$GAddAssetRequestInputs? _$v;

  String? _genesisPoint;
  String? get genesisPoint => _$this._genesisPoint;
  set genesisPoint(String? genesisPoint) => _$this._genesisPoint = genesisPoint;

  String? _name;
  String? get name => _$this._name;
  set name(String? name) => _$this._name = name;

  String? _metaData;
  String? get metaData => _$this._metaData;
  set metaData(String? metaData) => _$this._metaData = metaData;

  String? _assetId;
  String? get assetId => _$this._assetId;
  set assetId(String? assetId) => _$this._assetId = assetId;

  int? _outputIndex;
  int? get outputIndex => _$this._outputIndex;
  set outputIndex(int? outputIndex) => _$this._outputIndex = outputIndex;

  String? _proof;
  String? get proof => _$this._proof;
  set proof(String? proof) => _$this._proof = proof;

  GAddAssetRequestInputsBuilder();

  GAddAssetRequestInputsBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _genesisPoint = $v.genesisPoint;
      _name = $v.name;
      _metaData = $v.metaData;
      _assetId = $v.assetId;
      _outputIndex = $v.outputIndex;
      _proof = $v.proof;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GAddAssetRequestInputs other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GAddAssetRequestInputs;
  }

  @override
  void update(void Function(GAddAssetRequestInputsBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GAddAssetRequestInputs build() => _build();

  _$GAddAssetRequestInputs _build() {
    final _$result = _$v ??
        new _$GAddAssetRequestInputs._(
            genesisPoint: BuiltValueNullFieldError.checkNotNull(
                genesisPoint, r'GAddAssetRequestInputs', 'genesisPoint'),
            name: BuiltValueNullFieldError.checkNotNull(
                name, r'GAddAssetRequestInputs', 'name'),
            metaData: BuiltValueNullFieldError.checkNotNull(
                metaData, r'GAddAssetRequestInputs', 'metaData'),
            assetId: BuiltValueNullFieldError.checkNotNull(
                assetId, r'GAddAssetRequestInputs', 'assetId'),
            outputIndex: BuiltValueNullFieldError.checkNotNull(
                outputIndex, r'GAddAssetRequestInputs', 'outputIndex'),
            proof: BuiltValueNullFieldError.checkNotNull(
                proof, r'GAddAssetRequestInputs', 'proof'));
    replace(_$result);
    return _$result;
  }
}

class _$GBigDecimal extends GBigDecimal {
  @override
  final String value;

  factory _$GBigDecimal([void Function(GBigDecimalBuilder)? updates]) =>
      (new GBigDecimalBuilder()..update(updates))._build();

  _$GBigDecimal._({required this.value}) : super._() {
    BuiltValueNullFieldError.checkNotNull(value, r'GBigDecimal', 'value');
  }

  @override
  GBigDecimal rebuild(void Function(GBigDecimalBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GBigDecimalBuilder toBuilder() => new GBigDecimalBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GBigDecimal && value == other.value;
  }

  @override
  int get hashCode {
    return $jf($jc(0, value.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GBigDecimal')..add('value', value))
        .toString();
  }
}

class GBigDecimalBuilder implements Builder<GBigDecimal, GBigDecimalBuilder> {
  _$GBigDecimal? _$v;

  String? _value;
  String? get value => _$this._value;
  set value(String? value) => _$this._value = value;

  GBigDecimalBuilder();

  GBigDecimalBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _value = $v.value;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GBigDecimal other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GBigDecimal;
  }

  @override
  void update(void Function(GBigDecimalBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GBigDecimal build() => _build();

  _$GBigDecimal _build() {
    final _$result = _$v ??
        new _$GBigDecimal._(
            value: BuiltValueNullFieldError.checkNotNull(
                value, r'GBigDecimal', 'value'));
    replace(_$result);
    return _$result;
  }
}

class _$GBigInteger extends GBigInteger {
  @override
  final String value;

  factory _$GBigInteger([void Function(GBigIntegerBuilder)? updates]) =>
      (new GBigIntegerBuilder()..update(updates))._build();

  _$GBigInteger._({required this.value}) : super._() {
    BuiltValueNullFieldError.checkNotNull(value, r'GBigInteger', 'value');
  }

  @override
  GBigInteger rebuild(void Function(GBigIntegerBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GBigIntegerBuilder toBuilder() => new GBigIntegerBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GBigInteger && value == other.value;
  }

  @override
  int get hashCode {
    return $jf($jc(0, value.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GBigInteger')..add('value', value))
        .toString();
  }
}

class GBigIntegerBuilder implements Builder<GBigInteger, GBigIntegerBuilder> {
  _$GBigInteger? _$v;

  String? _value;
  String? get value => _$this._value;
  set value(String? value) => _$this._value = value;

  GBigIntegerBuilder();

  GBigIntegerBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _value = $v.value;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GBigInteger other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GBigInteger;
  }

  @override
  void update(void Function(GBigIntegerBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GBigInteger build() => _build();

  _$GBigInteger _build() {
    final _$result = _$v ??
        new _$GBigInteger._(
            value: BuiltValueNullFieldError.checkNotNull(
                value, r'GBigInteger', 'value'));
    replace(_$result);
    return _$result;
  }
}

class _$GDateTime extends GDateTime {
  @override
  final String value;

  factory _$GDateTime([void Function(GDateTimeBuilder)? updates]) =>
      (new GDateTimeBuilder()..update(updates))._build();

  _$GDateTime._({required this.value}) : super._() {
    BuiltValueNullFieldError.checkNotNull(value, r'GDateTime', 'value');
  }

  @override
  GDateTime rebuild(void Function(GDateTimeBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GDateTimeBuilder toBuilder() => new GDateTimeBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GDateTime && value == other.value;
  }

  @override
  int get hashCode {
    return $jf($jc(0, value.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GDateTime')..add('value', value))
        .toString();
  }
}

class GDateTimeBuilder implements Builder<GDateTime, GDateTimeBuilder> {
  _$GDateTime? _$v;

  String? _value;
  String? get value => _$this._value;
  set value(String? value) => _$this._value = value;

  GDateTimeBuilder();

  GDateTimeBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _value = $v.value;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GDateTime other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GDateTime;
  }

  @override
  void update(void Function(GDateTimeBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GDateTime build() => _build();

  _$GDateTime _build() {
    final _$result = _$v ??
        new _$GDateTime._(
            value: BuiltValueNullFieldError.checkNotNull(
                value, r'GDateTime', 'value'));
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: always_put_control_body_on_new_line,always_specify_types,annotate_overrides,avoid_annotating_with_dynamic,avoid_as,avoid_catches_without_on_clauses,avoid_returning_this,deprecated_member_use_from_same_package,lines_longer_than_80_chars,no_leading_underscores_for_local_identifiers,omit_local_variable_types,prefer_expression_function_bodies,sort_constructors_first,test_types_in_equals,unnecessary_const,unnecessary_new,unnecessary_lambdas
