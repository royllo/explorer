// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'queryAssets.data.gql.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

Serializer<GqueryAssetsData> _$gqueryAssetsDataSerializer =
    new _$GqueryAssetsDataSerializer();
Serializer<GqueryAssetsData_queryAssets>
    _$gqueryAssetsDataQueryAssetsSerializer =
    new _$GqueryAssetsData_queryAssetsSerializer();
Serializer<GqueryAssetsData_queryAssets_content>
    _$gqueryAssetsDataQueryAssetsContentSerializer =
    new _$GqueryAssetsData_queryAssets_contentSerializer();

class _$GqueryAssetsDataSerializer
    implements StructuredSerializer<GqueryAssetsData> {
  @override
  final Iterable<Type> types = const [GqueryAssetsData, _$GqueryAssetsData];
  @override
  final String wireName = 'GqueryAssetsData';

  @override
  Iterable<Object?> serialize(Serializers serializers, GqueryAssetsData object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
    ];
    Object? value;
    value = object.queryAssets;
    if (value != null) {
      result
        ..add('queryAssets')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(GqueryAssetsData_queryAssets)));
    }
    return result;
  }

  @override
  GqueryAssetsData deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GqueryAssetsDataBuilder();

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
        case 'queryAssets':
          result.queryAssets.replace(serializers.deserialize(value,
                  specifiedType: const FullType(GqueryAssetsData_queryAssets))!
              as GqueryAssetsData_queryAssets);
          break;
      }
    }

    return result.build();
  }
}

class _$GqueryAssetsData_queryAssetsSerializer
    implements StructuredSerializer<GqueryAssetsData_queryAssets> {
  @override
  final Iterable<Type> types = const [
    GqueryAssetsData_queryAssets,
    _$GqueryAssetsData_queryAssets
  ];
  @override
  final String wireName = 'GqueryAssetsData_queryAssets';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GqueryAssetsData_queryAssets object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
    ];
    Object? value;
    value = object.content;
    if (value != null) {
      result
        ..add('content')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(BuiltList, const [
              const FullType.nullable(GqueryAssetsData_queryAssets_content)
            ])));
    }
    return result;
  }

  @override
  GqueryAssetsData_queryAssets deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GqueryAssetsData_queryAssetsBuilder();

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
        case 'content':
          result.content.replace(serializers.deserialize(value,
              specifiedType: const FullType(BuiltList, const [
                const FullType.nullable(GqueryAssetsData_queryAssets_content)
              ]))! as BuiltList<Object?>);
          break;
      }
    }

    return result.build();
  }
}

class _$GqueryAssetsData_queryAssets_contentSerializer
    implements StructuredSerializer<GqueryAssetsData_queryAssets_content> {
  @override
  final Iterable<Type> types = const [
    GqueryAssetsData_queryAssets_content,
    _$GqueryAssetsData_queryAssets_content
  ];
  @override
  final String wireName = 'GqueryAssetsData_queryAssets_content';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GqueryAssetsData_queryAssets_content object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
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
    return result;
  }

  @override
  GqueryAssetsData_queryAssets_content deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GqueryAssetsData_queryAssets_contentBuilder();

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
        case 'name':
          result.name = serializers.deserialize(value,
              specifiedType: const FullType(String)) as String?;
          break;
      }
    }

    return result.build();
  }
}

class _$GqueryAssetsData extends GqueryAssetsData {
  @override
  final String G__typename;
  @override
  final GqueryAssetsData_queryAssets? queryAssets;

  factory _$GqueryAssetsData(
          [void Function(GqueryAssetsDataBuilder)? updates]) =>
      (new GqueryAssetsDataBuilder()..update(updates))._build();

  _$GqueryAssetsData._({required this.G__typename, this.queryAssets})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        G__typename, r'GqueryAssetsData', 'G__typename');
  }

  @override
  GqueryAssetsData rebuild(void Function(GqueryAssetsDataBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GqueryAssetsDataBuilder toBuilder() =>
      new GqueryAssetsDataBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GqueryAssetsData &&
        G__typename == other.G__typename &&
        queryAssets == other.queryAssets;
  }

  @override
  int get hashCode {
    return $jf($jc($jc(0, G__typename.hashCode), queryAssets.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GqueryAssetsData')
          ..add('G__typename', G__typename)
          ..add('queryAssets', queryAssets))
        .toString();
  }
}

class GqueryAssetsDataBuilder
    implements Builder<GqueryAssetsData, GqueryAssetsDataBuilder> {
  _$GqueryAssetsData? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  GqueryAssetsData_queryAssetsBuilder? _queryAssets;
  GqueryAssetsData_queryAssetsBuilder get queryAssets =>
      _$this._queryAssets ??= new GqueryAssetsData_queryAssetsBuilder();
  set queryAssets(GqueryAssetsData_queryAssetsBuilder? queryAssets) =>
      _$this._queryAssets = queryAssets;

  GqueryAssetsDataBuilder() {
    GqueryAssetsData._initializeBuilder(this);
  }

  GqueryAssetsDataBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _queryAssets = $v.queryAssets?.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GqueryAssetsData other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GqueryAssetsData;
  }

  @override
  void update(void Function(GqueryAssetsDataBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GqueryAssetsData build() => _build();

  _$GqueryAssetsData _build() {
    _$GqueryAssetsData _$result;
    try {
      _$result = _$v ??
          new _$GqueryAssetsData._(
              G__typename: BuiltValueNullFieldError.checkNotNull(
                  G__typename, r'GqueryAssetsData', 'G__typename'),
              queryAssets: _queryAssets?.build());
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'queryAssets';
        _queryAssets?.build();
      } catch (e) {
        throw new BuiltValueNestedFieldError(
            r'GqueryAssetsData', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

class _$GqueryAssetsData_queryAssets extends GqueryAssetsData_queryAssets {
  @override
  final String G__typename;
  @override
  final BuiltList<GqueryAssetsData_queryAssets_content?>? content;

  factory _$GqueryAssetsData_queryAssets(
          [void Function(GqueryAssetsData_queryAssetsBuilder)? updates]) =>
      (new GqueryAssetsData_queryAssetsBuilder()..update(updates))._build();

  _$GqueryAssetsData_queryAssets._({required this.G__typename, this.content})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        G__typename, r'GqueryAssetsData_queryAssets', 'G__typename');
  }

  @override
  GqueryAssetsData_queryAssets rebuild(
          void Function(GqueryAssetsData_queryAssetsBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GqueryAssetsData_queryAssetsBuilder toBuilder() =>
      new GqueryAssetsData_queryAssetsBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GqueryAssetsData_queryAssets &&
        G__typename == other.G__typename &&
        content == other.content;
  }

  @override
  int get hashCode {
    return $jf($jc($jc(0, G__typename.hashCode), content.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GqueryAssetsData_queryAssets')
          ..add('G__typename', G__typename)
          ..add('content', content))
        .toString();
  }
}

class GqueryAssetsData_queryAssetsBuilder
    implements
        Builder<GqueryAssetsData_queryAssets,
            GqueryAssetsData_queryAssetsBuilder> {
  _$GqueryAssetsData_queryAssets? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  ListBuilder<GqueryAssetsData_queryAssets_content?>? _content;
  ListBuilder<GqueryAssetsData_queryAssets_content?> get content =>
      _$this._content ??=
          new ListBuilder<GqueryAssetsData_queryAssets_content?>();
  set content(ListBuilder<GqueryAssetsData_queryAssets_content?>? content) =>
      _$this._content = content;

  GqueryAssetsData_queryAssetsBuilder() {
    GqueryAssetsData_queryAssets._initializeBuilder(this);
  }

  GqueryAssetsData_queryAssetsBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _content = $v.content?.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GqueryAssetsData_queryAssets other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GqueryAssetsData_queryAssets;
  }

  @override
  void update(void Function(GqueryAssetsData_queryAssetsBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GqueryAssetsData_queryAssets build() => _build();

  _$GqueryAssetsData_queryAssets _build() {
    _$GqueryAssetsData_queryAssets _$result;
    try {
      _$result = _$v ??
          new _$GqueryAssetsData_queryAssets._(
              G__typename: BuiltValueNullFieldError.checkNotNull(
                  G__typename, r'GqueryAssetsData_queryAssets', 'G__typename'),
              content: _content?.build());
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'content';
        _content?.build();
      } catch (e) {
        throw new BuiltValueNestedFieldError(
            r'GqueryAssetsData_queryAssets', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

class _$GqueryAssetsData_queryAssets_content
    extends GqueryAssetsData_queryAssets_content {
  @override
  final String G__typename;
  @override
  final String? assetId;
  @override
  final String? name;

  factory _$GqueryAssetsData_queryAssets_content(
          [void Function(GqueryAssetsData_queryAssets_contentBuilder)?
              updates]) =>
      (new GqueryAssetsData_queryAssets_contentBuilder()..update(updates))
          ._build();

  _$GqueryAssetsData_queryAssets_content._(
      {required this.G__typename, this.assetId, this.name})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        G__typename, r'GqueryAssetsData_queryAssets_content', 'G__typename');
  }

  @override
  GqueryAssetsData_queryAssets_content rebuild(
          void Function(GqueryAssetsData_queryAssets_contentBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GqueryAssetsData_queryAssets_contentBuilder toBuilder() =>
      new GqueryAssetsData_queryAssets_contentBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GqueryAssetsData_queryAssets_content &&
        G__typename == other.G__typename &&
        assetId == other.assetId &&
        name == other.name;
  }

  @override
  int get hashCode {
    return $jf($jc(
        $jc($jc(0, G__typename.hashCode), assetId.hashCode), name.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GqueryAssetsData_queryAssets_content')
          ..add('G__typename', G__typename)
          ..add('assetId', assetId)
          ..add('name', name))
        .toString();
  }
}

class GqueryAssetsData_queryAssets_contentBuilder
    implements
        Builder<GqueryAssetsData_queryAssets_content,
            GqueryAssetsData_queryAssets_contentBuilder> {
  _$GqueryAssetsData_queryAssets_content? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  String? _assetId;
  String? get assetId => _$this._assetId;
  set assetId(String? assetId) => _$this._assetId = assetId;

  String? _name;
  String? get name => _$this._name;
  set name(String? name) => _$this._name = name;

  GqueryAssetsData_queryAssets_contentBuilder() {
    GqueryAssetsData_queryAssets_content._initializeBuilder(this);
  }

  GqueryAssetsData_queryAssets_contentBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _assetId = $v.assetId;
      _name = $v.name;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GqueryAssetsData_queryAssets_content other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GqueryAssetsData_queryAssets_content;
  }

  @override
  void update(
      void Function(GqueryAssetsData_queryAssets_contentBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GqueryAssetsData_queryAssets_content build() => _build();

  _$GqueryAssetsData_queryAssets_content _build() {
    final _$result = _$v ??
        new _$GqueryAssetsData_queryAssets_content._(
            G__typename: BuiltValueNullFieldError.checkNotNull(G__typename,
                r'GqueryAssetsData_queryAssets_content', 'G__typename'),
            assetId: assetId,
            name: name);
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: always_put_control_body_on_new_line,always_specify_types,annotate_overrides,avoid_annotating_with_dynamic,avoid_as,avoid_catches_without_on_clauses,avoid_returning_this,deprecated_member_use_from_same_package,lines_longer_than_80_chars,no_leading_underscores_for_local_identifiers,omit_local_variable_types,prefer_expression_function_bodies,sort_constructors_first,test_types_in_equals,unnecessary_const,unnecessary_new,unnecessary_lambdas
