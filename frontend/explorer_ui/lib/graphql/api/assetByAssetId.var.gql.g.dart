// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'assetByAssetId.var.gql.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

Serializer<GassetByAssetIdVars> _$gassetByAssetIdVarsSerializer =
    new _$GassetByAssetIdVarsSerializer();

class _$GassetByAssetIdVarsSerializer
    implements StructuredSerializer<GassetByAssetIdVars> {
  @override
  final Iterable<Type> types = const [
    GassetByAssetIdVars,
    _$GassetByAssetIdVars
  ];
  @override
  final String wireName = 'GassetByAssetIdVars';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GassetByAssetIdVars object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      'assetId',
      serializers.serialize(object.assetId,
          specifiedType: const FullType(String)),
    ];

    return result;
  }

  @override
  GassetByAssetIdVars deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GassetByAssetIdVarsBuilder();

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
      }
    }

    return result.build();
  }
}

class _$GassetByAssetIdVars extends GassetByAssetIdVars {
  @override
  final String assetId;

  factory _$GassetByAssetIdVars(
          [void Function(GassetByAssetIdVarsBuilder)? updates]) =>
      (new GassetByAssetIdVarsBuilder()..update(updates))._build();

  _$GassetByAssetIdVars._({required this.assetId}) : super._() {
    BuiltValueNullFieldError.checkNotNull(
        assetId, r'GassetByAssetIdVars', 'assetId');
  }

  @override
  GassetByAssetIdVars rebuild(
          void Function(GassetByAssetIdVarsBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GassetByAssetIdVarsBuilder toBuilder() =>
      new GassetByAssetIdVarsBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GassetByAssetIdVars && assetId == other.assetId;
  }

  @override
  int get hashCode {
    return $jf($jc(0, assetId.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GassetByAssetIdVars')
          ..add('assetId', assetId))
        .toString();
  }
}

class GassetByAssetIdVarsBuilder
    implements Builder<GassetByAssetIdVars, GassetByAssetIdVarsBuilder> {
  _$GassetByAssetIdVars? _$v;

  String? _assetId;
  String? get assetId => _$this._assetId;
  set assetId(String? assetId) => _$this._assetId = assetId;

  GassetByAssetIdVarsBuilder();

  GassetByAssetIdVarsBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _assetId = $v.assetId;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GassetByAssetIdVars other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GassetByAssetIdVars;
  }

  @override
  void update(void Function(GassetByAssetIdVarsBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GassetByAssetIdVars build() => _build();

  _$GassetByAssetIdVars _build() {
    final _$result = _$v ??
        new _$GassetByAssetIdVars._(
            assetId: BuiltValueNullFieldError.checkNotNull(
                assetId, r'GassetByAssetIdVars', 'assetId'));
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: always_put_control_body_on_new_line,always_specify_types,annotate_overrides,avoid_annotating_with_dynamic,avoid_as,avoid_catches_without_on_clauses,avoid_returning_this,deprecated_member_use_from_same_package,lines_longer_than_80_chars,no_leading_underscores_for_local_identifiers,omit_local_variable_types,prefer_expression_function_bodies,sort_constructors_first,test_types_in_equals,unnecessary_const,unnecessary_new,unnecessary_lambdas
