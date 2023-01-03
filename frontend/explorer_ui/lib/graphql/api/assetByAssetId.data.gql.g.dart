// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'assetByAssetId.data.gql.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

Serializer<GassetByAssetIdData> _$gassetByAssetIdDataSerializer =
    new _$GassetByAssetIdDataSerializer();
Serializer<GassetByAssetIdData_assetByAssetId>
    _$gassetByAssetIdDataAssetByAssetIdSerializer =
    new _$GassetByAssetIdData_assetByAssetIdSerializer();
Serializer<GassetByAssetIdData_assetByAssetId_genesisPoint>
    _$gassetByAssetIdDataAssetByAssetIdGenesisPointSerializer =
    new _$GassetByAssetIdData_assetByAssetId_genesisPointSerializer();
Serializer<GassetByAssetIdData_assetByAssetId_creator>
    _$gassetByAssetIdDataAssetByAssetIdCreatorSerializer =
    new _$GassetByAssetIdData_assetByAssetId_creatorSerializer();

class _$GassetByAssetIdDataSerializer
    implements StructuredSerializer<GassetByAssetIdData> {
  @override
  final Iterable<Type> types = const [
    GassetByAssetIdData,
    _$GassetByAssetIdData
  ];
  @override
  final String wireName = 'GassetByAssetIdData';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GassetByAssetIdData object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
    ];
    Object? value;
    value = object.assetByAssetId;
    if (value != null) {
      result
        ..add('assetByAssetId')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(GassetByAssetIdData_assetByAssetId)));
    }
    return result;
  }

  @override
  GassetByAssetIdData deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GassetByAssetIdDataBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case '__typename':
          result.G__typename = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'assetByAssetId':
          result.assetByAssetId.replace(serializers.deserialize(value,
                  specifiedType:
                      const FullType(GassetByAssetIdData_assetByAssetId))!
              as GassetByAssetIdData_assetByAssetId);
          break;
      }
    }

    return result.build();
  }
}

class _$GassetByAssetIdData_assetByAssetIdSerializer
    implements StructuredSerializer<GassetByAssetIdData_assetByAssetId> {
  @override
  final Iterable<Type> types = const [
    GassetByAssetIdData_assetByAssetId,
    _$GassetByAssetIdData_assetByAssetId
  ];
  @override
  final String wireName = 'GassetByAssetIdData_assetByAssetId';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GassetByAssetIdData_assetByAssetId object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
      'genesisPoint',
      serializers.serialize(object.genesisPoint,
          specifiedType:
              const FullType(GassetByAssetIdData_assetByAssetId_genesisPoint)),
      'creator',
      serializers.serialize(object.creator,
          specifiedType:
              const FullType(GassetByAssetIdData_assetByAssetId_creator)),
    ];
    Object? value;
    value = object.assetId;
    if (value != null) {
      result
        ..add('assetId')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    value = object.name;
    if (value != null) {
      result
        ..add('name')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    value = object.metaData;
    if (value != null) {
      result
        ..add('metaData')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    value = object.outputIndex;
    if (value != null) {
      result
        ..add('outputIndex')
        ..add(serializers.serialize(value, specifiedType: const FullType(int)));
    }
    return result;
  }

  @override
  GassetByAssetIdData_assetByAssetId deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GassetByAssetIdData_assetByAssetIdBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case '__typename':
          result.G__typename = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'assetId':
          result.assetId = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
        case 'genesisPoint':
          result.genesisPoint.replace(serializers.deserialize(value,
                  specifiedType: const FullType(
                      GassetByAssetIdData_assetByAssetId_genesisPoint))!
              as GassetByAssetIdData_assetByAssetId_genesisPoint);
          break;
        case 'creator':
          result.creator.replace(serializers.deserialize(value,
                  specifiedType: const FullType(
                      GassetByAssetIdData_assetByAssetId_creator))!
              as GassetByAssetIdData_assetByAssetId_creator);
          break;
        case 'name':
          result.name = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
        case 'metaData':
          result.metaData = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
        case 'outputIndex':
          result.outputIndex = serializers.deserialize(value,
              specifiedType: const FullType(int)) as int?;
          break;
      }
    }

    return result.build();
  }
}

class _$GassetByAssetIdData_assetByAssetId_genesisPointSerializer
    implements
        StructuredSerializer<GassetByAssetIdData_assetByAssetId_genesisPoint> {
  @override
  final Iterable<Type> types = const [
    GassetByAssetIdData_assetByAssetId_genesisPoint,
    _$GassetByAssetIdData_assetByAssetId_genesisPoint
  ];
  @override
  final String wireName = 'GassetByAssetIdData_assetByAssetId_genesisPoint';

  @override
  Iterable<Object?> serialize(Serializers serializers,
      GassetByAssetIdData_assetByAssetId_genesisPoint object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
      'txId',
      serializers.serialize(object.txId, specifiedType: const FullType(String)),
      'vout',
      serializers.serialize(object.vout, specifiedType: const FullType(int)),
    ];
    Object? value;
    value = object.blockHeight;
    if (value != null) {
      result
        ..add('blockHeight')
        ..add(serializers.serialize(value, specifiedType: const FullType(int)));
    }
    value = object.scriptPubKey;
    if (value != null) {
      result
        ..add('scriptPubKey')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    value = object.scriptPubKeyAsm;
    if (value != null) {
      result
        ..add('scriptPubKeyAsm')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    value = object.scriptPubKeyType;
    if (value != null) {
      result
        ..add('scriptPubKeyType')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    value = object.scriptPubKeyAddress;
    if (value != null) {
      result
        ..add('scriptPubKeyAddress')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(String)));
    }
    return result;
  }

  @override
  GassetByAssetIdData_assetByAssetId_genesisPoint deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GassetByAssetIdData_assetByAssetId_genesisPointBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case '__typename':
          result.G__typename = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'blockHeight':
          result.blockHeight = serializers.deserialize(value,
              specifiedType: const FullType(int)) as int?;
          break;
        case 'txId':
          result.txId = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'vout':
          result.vout = serializers.deserialize(value,
              specifiedType: const FullType(int))! as int;
          break;
        case 'scriptPubKey':
          result.scriptPubKey = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
        case 'scriptPubKeyAsm':
          result.scriptPubKeyAsm = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
        case 'scriptPubKeyType':
          result.scriptPubKeyType = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
        case 'scriptPubKeyAddress':
          result.scriptPubKeyAddress = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
      }
    }

    return result.build();
  }
}

class _$GassetByAssetIdData_assetByAssetId_creatorSerializer
    implements
        StructuredSerializer<GassetByAssetIdData_assetByAssetId_creator> {
  @override
  final Iterable<Type> types = const [
    GassetByAssetIdData_assetByAssetId_creator,
    _$GassetByAssetIdData_assetByAssetId_creator
  ];
  @override
  final String wireName = 'GassetByAssetIdData_assetByAssetId_creator';

  @override
  Iterable<Object?> serialize(Serializers serializers,
      GassetByAssetIdData_assetByAssetId_creator object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
      'id',
      serializers.serialize(object.id, specifiedType: const FullType(String)),
      'username',
      serializers.serialize(object.username,
          specifiedType: const FullType(String)),
    ];

    return result;
  }

  @override
  GassetByAssetIdData_assetByAssetId_creator deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GassetByAssetIdData_assetByAssetId_creatorBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case '__typename':
          result.G__typename = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'id':
          result.id = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'username':
          result.username = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
      }
    }

    return result.build();
  }
}

class _$GassetByAssetIdData extends GassetByAssetIdData {
  @override
  final String G__typename;
  @override
  final GassetByAssetIdData_assetByAssetId? assetByAssetId;

  factory _$GassetByAssetIdData(
          [void Function(GassetByAssetIdDataBuilder)? updates]) =>
      (new GassetByAssetIdDataBuilder()..update(updates))._build();

  _$GassetByAssetIdData._({required this.G__typename, this.assetByAssetId})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        G__typename, r'GassetByAssetIdData', 'G__typename');
  }

  @override
  GassetByAssetIdData rebuild(
          void Function(GassetByAssetIdDataBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GassetByAssetIdDataBuilder toBuilder() =>
      new GassetByAssetIdDataBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GassetByAssetIdData &&
        G__typename == other.G__typename &&
        assetByAssetId == other.assetByAssetId;
  }

  @override
  int get hashCode {
    return $jf($jc($jc(0, G__typename.hashCode), assetByAssetId.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GassetByAssetIdData')
          ..add('G__typename', G__typename)
          ..add('assetByAssetId', assetByAssetId))
        .toString();
  }
}

class GassetByAssetIdDataBuilder
    implements Builder<GassetByAssetIdData, GassetByAssetIdDataBuilder> {
  _$GassetByAssetIdData? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  GassetByAssetIdData_assetByAssetIdBuilder? _assetByAssetId;
  GassetByAssetIdData_assetByAssetIdBuilder get assetByAssetId =>
      _$this._assetByAssetId ??=
          new GassetByAssetIdData_assetByAssetIdBuilder();
  set assetByAssetId(
          GassetByAssetIdData_assetByAssetIdBuilder? assetByAssetId) =>
      _$this._assetByAssetId = assetByAssetId;

  GassetByAssetIdDataBuilder() {
    GassetByAssetIdData._initializeBuilder(this);
  }

  GassetByAssetIdDataBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _assetByAssetId = $v.assetByAssetId?.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GassetByAssetIdData other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GassetByAssetIdData;
  }

  @override
  void update(void Function(GassetByAssetIdDataBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GassetByAssetIdData build() => _build();

  _$GassetByAssetIdData _build() {
    _$GassetByAssetIdData _$result;
    try {
      _$result = _$v ??
          new _$GassetByAssetIdData._(
              G__typename: BuiltValueNullFieldError.checkNotNull(
                  G__typename, r'GassetByAssetIdData', 'G__typename'),
              assetByAssetId: _assetByAssetId?.build());
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'assetByAssetId';
        _assetByAssetId?.build();
      } catch (e) {
        throw new BuiltValueNestedFieldError(
            r'GassetByAssetIdData', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

class _$GassetByAssetIdData_assetByAssetId
    extends GassetByAssetIdData_assetByAssetId {
  @override
  final String G__typename;
  @override
  final String? assetId;
  @override
  final GassetByAssetIdData_assetByAssetId_genesisPoint genesisPoint;
  @override
  final GassetByAssetIdData_assetByAssetId_creator creator;
  @override
  final String? name;
  @override
  final String? metaData;
  @override
  final int? outputIndex;

  factory _$GassetByAssetIdData_assetByAssetId(
          [void Function(GassetByAssetIdData_assetByAssetIdBuilder)?
              updates]) =>
      (new GassetByAssetIdData_assetByAssetIdBuilder()..update(updates))
          ._build();

  _$GassetByAssetIdData_assetByAssetId._(
      {required this.G__typename,
      this.assetId,
      required this.genesisPoint,
      required this.creator,
      this.name,
      this.metaData,
      this.outputIndex})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        G__typename, r'GassetByAssetIdData_assetByAssetId', 'G__typename');
    BuiltValueNullFieldError.checkNotNull(
        genesisPoint, r'GassetByAssetIdData_assetByAssetId', 'genesisPoint');
    BuiltValueNullFieldError.checkNotNull(
        creator, r'GassetByAssetIdData_assetByAssetId', 'creator');
  }

  @override
  GassetByAssetIdData_assetByAssetId rebuild(
          void Function(GassetByAssetIdData_assetByAssetIdBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GassetByAssetIdData_assetByAssetIdBuilder toBuilder() =>
      new GassetByAssetIdData_assetByAssetIdBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GassetByAssetIdData_assetByAssetId &&
        G__typename == other.G__typename &&
        assetId == other.assetId &&
        genesisPoint == other.genesisPoint &&
        creator == other.creator &&
        name == other.name &&
        metaData == other.metaData &&
        outputIndex == other.outputIndex;
  }

  @override
  int get hashCode {
    return $jf($jc(
        $jc(
            $jc(
                $jc(
                    $jc($jc($jc(0, G__typename.hashCode), assetId.hashCode),
                        genesisPoint.hashCode),
                    creator.hashCode),
                name.hashCode),
            metaData.hashCode),
        outputIndex.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GassetByAssetIdData_assetByAssetId')
          ..add('G__typename', G__typename)
          ..add('assetId', assetId)
          ..add('genesisPoint', genesisPoint)
          ..add('creator', creator)
          ..add('name', name)
          ..add('metaData', metaData)
          ..add('outputIndex', outputIndex))
        .toString();
  }
}

class GassetByAssetIdData_assetByAssetIdBuilder
    implements
        Builder<GassetByAssetIdData_assetByAssetId,
            GassetByAssetIdData_assetByAssetIdBuilder> {
  _$GassetByAssetIdData_assetByAssetId? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  String? _assetId;
  String? get assetId => _$this._assetId;
  set assetId(String? assetId) => _$this._assetId = assetId;

  GassetByAssetIdData_assetByAssetId_genesisPointBuilder? _genesisPoint;
  GassetByAssetIdData_assetByAssetId_genesisPointBuilder get genesisPoint =>
      _$this._genesisPoint ??=
          new GassetByAssetIdData_assetByAssetId_genesisPointBuilder();
  set genesisPoint(
          GassetByAssetIdData_assetByAssetId_genesisPointBuilder?
              genesisPoint) =>
      _$this._genesisPoint = genesisPoint;

  GassetByAssetIdData_assetByAssetId_creatorBuilder? _creator;
  GassetByAssetIdData_assetByAssetId_creatorBuilder get creator =>
      _$this._creator ??=
          new GassetByAssetIdData_assetByAssetId_creatorBuilder();
  set creator(GassetByAssetIdData_assetByAssetId_creatorBuilder? creator) =>
      _$this._creator = creator;

  String? _name;
  String? get name => _$this._name;
  set name(String? name) => _$this._name = name;

  String? _metaData;
  String? get metaData => _$this._metaData;
  set metaData(String? metaData) => _$this._metaData = metaData;

  int? _outputIndex;
  int? get outputIndex => _$this._outputIndex;
  set outputIndex(int? outputIndex) => _$this._outputIndex = outputIndex;

  GassetByAssetIdData_assetByAssetIdBuilder() {
    GassetByAssetIdData_assetByAssetId._initializeBuilder(this);
  }

  GassetByAssetIdData_assetByAssetIdBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _assetId = $v.assetId;
      _genesisPoint = $v.genesisPoint.toBuilder();
      _creator = $v.creator.toBuilder();
      _name = $v.name;
      _metaData = $v.metaData;
      _outputIndex = $v.outputIndex;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GassetByAssetIdData_assetByAssetId other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GassetByAssetIdData_assetByAssetId;
  }

  @override
  void update(
      void Function(GassetByAssetIdData_assetByAssetIdBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GassetByAssetIdData_assetByAssetId build() => _build();

  _$GassetByAssetIdData_assetByAssetId _build() {
    _$GassetByAssetIdData_assetByAssetId _$result;
    try {
      _$result = _$v ??
          new _$GassetByAssetIdData_assetByAssetId._(
              G__typename: BuiltValueNullFieldError.checkNotNull(G__typename,
                  r'GassetByAssetIdData_assetByAssetId', 'G__typename'),
              assetId: assetId,
              genesisPoint: genesisPoint.build(),
              creator: creator.build(),
              name: name,
              metaData: metaData,
              outputIndex: outputIndex);
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'genesisPoint';
        genesisPoint.build();
        _$failedField = 'creator';
        creator.build();
      } catch (e) {
        throw new BuiltValueNestedFieldError(
            r'GassetByAssetIdData_assetByAssetId', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

class _$GassetByAssetIdData_assetByAssetId_genesisPoint
    extends GassetByAssetIdData_assetByAssetId_genesisPoint {
  @override
  final String G__typename;
  @override
  final int? blockHeight;
  @override
  final String txId;
  @override
  final int vout;
  @override
  final String? scriptPubKey;
  @override
  final String? scriptPubKeyAsm;
  @override
  final String? scriptPubKeyType;
  @override
  final String? scriptPubKeyAddress;

  factory _$GassetByAssetIdData_assetByAssetId_genesisPoint(
          [void Function(
                  GassetByAssetIdData_assetByAssetId_genesisPointBuilder)?
              updates]) =>
      (new GassetByAssetIdData_assetByAssetId_genesisPointBuilder()
            ..update(updates))
          ._build();

  _$GassetByAssetIdData_assetByAssetId_genesisPoint._(
      {required this.G__typename,
      this.blockHeight,
      required this.txId,
      required this.vout,
      this.scriptPubKey,
      this.scriptPubKeyAsm,
      this.scriptPubKeyType,
      this.scriptPubKeyAddress})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(G__typename,
        r'GassetByAssetIdData_assetByAssetId_genesisPoint', 'G__typename');
    BuiltValueNullFieldError.checkNotNull(
        txId, r'GassetByAssetIdData_assetByAssetId_genesisPoint', 'txId');
    BuiltValueNullFieldError.checkNotNull(
        vout, r'GassetByAssetIdData_assetByAssetId_genesisPoint', 'vout');
  }

  @override
  GassetByAssetIdData_assetByAssetId_genesisPoint rebuild(
          void Function(GassetByAssetIdData_assetByAssetId_genesisPointBuilder)
              updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GassetByAssetIdData_assetByAssetId_genesisPointBuilder toBuilder() =>
      new GassetByAssetIdData_assetByAssetId_genesisPointBuilder()
        ..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GassetByAssetIdData_assetByAssetId_genesisPoint &&
        G__typename == other.G__typename &&
        blockHeight == other.blockHeight &&
        txId == other.txId &&
        vout == other.vout &&
        scriptPubKey == other.scriptPubKey &&
        scriptPubKeyAsm == other.scriptPubKeyAsm &&
        scriptPubKeyType == other.scriptPubKeyType &&
        scriptPubKeyAddress == other.scriptPubKeyAddress;
  }

  @override
  int get hashCode {
    return $jf($jc(
        $jc(
            $jc(
                $jc(
                    $jc(
                        $jc(
                            $jc($jc(0, G__typename.hashCode),
                                blockHeight.hashCode),
                            txId.hashCode),
                        vout.hashCode),
                    scriptPubKey.hashCode),
                scriptPubKeyAsm.hashCode),
            scriptPubKeyType.hashCode),
        scriptPubKeyAddress.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(
            r'GassetByAssetIdData_assetByAssetId_genesisPoint')
          ..add('G__typename', G__typename)
          ..add('blockHeight', blockHeight)
          ..add('txId', txId)
          ..add('vout', vout)
          ..add('scriptPubKey', scriptPubKey)
          ..add('scriptPubKeyAsm', scriptPubKeyAsm)
          ..add('scriptPubKeyType', scriptPubKeyType)
          ..add('scriptPubKeyAddress', scriptPubKeyAddress))
        .toString();
  }
}

class GassetByAssetIdData_assetByAssetId_genesisPointBuilder
    implements
        Builder<GassetByAssetIdData_assetByAssetId_genesisPoint,
            GassetByAssetIdData_assetByAssetId_genesisPointBuilder> {
  _$GassetByAssetIdData_assetByAssetId_genesisPoint? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  int? _blockHeight;
  int? get blockHeight => _$this._blockHeight;
  set blockHeight(int? blockHeight) => _$this._blockHeight = blockHeight;

  String? _txId;
  String? get txId => _$this._txId;
  set txId(String? txId) => _$this._txId = txId;

  int? _vout;
  int? get vout => _$this._vout;
  set vout(int? vout) => _$this._vout = vout;

  String? _scriptPubKey;
  String? get scriptPubKey => _$this._scriptPubKey;
  set scriptPubKey(String? scriptPubKey) => _$this._scriptPubKey = scriptPubKey;

  String? _scriptPubKeyAsm;
  String? get scriptPubKeyAsm => _$this._scriptPubKeyAsm;
  set scriptPubKeyAsm(String? scriptPubKeyAsm) =>
      _$this._scriptPubKeyAsm = scriptPubKeyAsm;

  String? _scriptPubKeyType;
  String? get scriptPubKeyType => _$this._scriptPubKeyType;
  set scriptPubKeyType(String? scriptPubKeyType) =>
      _$this._scriptPubKeyType = scriptPubKeyType;

  String? _scriptPubKeyAddress;
  String? get scriptPubKeyAddress => _$this._scriptPubKeyAddress;
  set scriptPubKeyAddress(String? scriptPubKeyAddress) =>
      _$this._scriptPubKeyAddress = scriptPubKeyAddress;

  GassetByAssetIdData_assetByAssetId_genesisPointBuilder() {
    GassetByAssetIdData_assetByAssetId_genesisPoint._initializeBuilder(this);
  }

  GassetByAssetIdData_assetByAssetId_genesisPointBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _blockHeight = $v.blockHeight;
      _txId = $v.txId;
      _vout = $v.vout;
      _scriptPubKey = $v.scriptPubKey;
      _scriptPubKeyAsm = $v.scriptPubKeyAsm;
      _scriptPubKeyType = $v.scriptPubKeyType;
      _scriptPubKeyAddress = $v.scriptPubKeyAddress;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GassetByAssetIdData_assetByAssetId_genesisPoint other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GassetByAssetIdData_assetByAssetId_genesisPoint;
  }

  @override
  void update(
      void Function(GassetByAssetIdData_assetByAssetId_genesisPointBuilder)?
          updates) {
    if (updates != null) updates(this);
  }

  @override
  GassetByAssetIdData_assetByAssetId_genesisPoint build() => _build();

  _$GassetByAssetIdData_assetByAssetId_genesisPoint _build() {
    final _$result = _$v ??
        new _$GassetByAssetIdData_assetByAssetId_genesisPoint._(
            G__typename: BuiltValueNullFieldError.checkNotNull(
                G__typename,
                r'GassetByAssetIdData_assetByAssetId_genesisPoint',
                'G__typename'),
            blockHeight: blockHeight,
            txId: BuiltValueNullFieldError.checkNotNull(txId,
                r'GassetByAssetIdData_assetByAssetId_genesisPoint', 'txId'),
            vout: BuiltValueNullFieldError.checkNotNull(vout,
                r'GassetByAssetIdData_assetByAssetId_genesisPoint', 'vout'),
            scriptPubKey: scriptPubKey,
            scriptPubKeyAsm: scriptPubKeyAsm,
            scriptPubKeyType: scriptPubKeyType,
            scriptPubKeyAddress: scriptPubKeyAddress);
    replace(_$result);
    return _$result;
  }
}

class _$GassetByAssetIdData_assetByAssetId_creator
    extends GassetByAssetIdData_assetByAssetId_creator {
  @override
  final String G__typename;
  @override
  final String id;
  @override
  final String username;

  factory _$GassetByAssetIdData_assetByAssetId_creator(
          [void Function(GassetByAssetIdData_assetByAssetId_creatorBuilder)?
              updates]) =>
      (new GassetByAssetIdData_assetByAssetId_creatorBuilder()..update(updates))
          ._build();

  _$GassetByAssetIdData_assetByAssetId_creator._(
      {required this.G__typename, required this.id, required this.username})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(G__typename,
        r'GassetByAssetIdData_assetByAssetId_creator', 'G__typename');
    BuiltValueNullFieldError.checkNotNull(
        id, r'GassetByAssetIdData_assetByAssetId_creator', 'id');
    BuiltValueNullFieldError.checkNotNull(
        username, r'GassetByAssetIdData_assetByAssetId_creator', 'username');
  }

  @override
  GassetByAssetIdData_assetByAssetId_creator rebuild(
          void Function(GassetByAssetIdData_assetByAssetId_creatorBuilder)
              updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GassetByAssetIdData_assetByAssetId_creatorBuilder toBuilder() =>
      new GassetByAssetIdData_assetByAssetId_creatorBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GassetByAssetIdData_assetByAssetId_creator &&
        G__typename == other.G__typename &&
        id == other.id &&
        username == other.username;
  }

  @override
  int get hashCode {
    return $jf(
        $jc($jc($jc(0, G__typename.hashCode), id.hashCode), username.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(
            r'GassetByAssetIdData_assetByAssetId_creator')
          ..add('G__typename', G__typename)
          ..add('id', id)
          ..add('username', username))
        .toString();
  }
}

class GassetByAssetIdData_assetByAssetId_creatorBuilder
    implements
        Builder<GassetByAssetIdData_assetByAssetId_creator,
            GassetByAssetIdData_assetByAssetId_creatorBuilder> {
  _$GassetByAssetIdData_assetByAssetId_creator? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  String? _id;
  String? get id => _$this._id;
  set id(String? id) => _$this._id = id;

  String? _username;
  String? get username => _$this._username;
  set username(String? username) => _$this._username = username;

  GassetByAssetIdData_assetByAssetId_creatorBuilder() {
    GassetByAssetIdData_assetByAssetId_creator._initializeBuilder(this);
  }

  GassetByAssetIdData_assetByAssetId_creatorBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _id = $v.id;
      _username = $v.username;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GassetByAssetIdData_assetByAssetId_creator other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GassetByAssetIdData_assetByAssetId_creator;
  }

  @override
  void update(
      void Function(GassetByAssetIdData_assetByAssetId_creatorBuilder)?
          updates) {
    if (updates != null) updates(this);
  }

  @override
  GassetByAssetIdData_assetByAssetId_creator build() => _build();

  _$GassetByAssetIdData_assetByAssetId_creator _build() {
    final _$result = _$v ??
        new _$GassetByAssetIdData_assetByAssetId_creator._(
            G__typename: BuiltValueNullFieldError.checkNotNull(G__typename,
                r'GassetByAssetIdData_assetByAssetId_creator', 'G__typename'),
            id: BuiltValueNullFieldError.checkNotNull(
                id, r'GassetByAssetIdData_assetByAssetId_creator', 'id'),
            username: BuiltValueNullFieldError.checkNotNull(username,
                r'GassetByAssetIdData_assetByAssetId_creator', 'username'));
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: always_put_control_body_on_new_line,always_specify_types,annotate_overrides,avoid_annotating_with_dynamic,avoid_as,avoid_catches_without_on_clauses,avoid_returning_this,deprecated_member_use_from_same_package,lines_longer_than_80_chars,no_leading_underscores_for_local_identifiers,omit_local_variable_types,prefer_expression_function_bodies,sort_constructors_first,test_types_in_equals,unnecessary_const,unnecessary_new,unnecessary_lambdas
