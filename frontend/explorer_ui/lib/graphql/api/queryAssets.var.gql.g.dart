// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'queryAssets.var.gql.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

Serializer<GqueryAssetsVars> _$gqueryAssetsVarsSerializer =
    new _$GqueryAssetsVarsSerializer();

class _$GqueryAssetsVarsSerializer
    implements StructuredSerializer<GqueryAssetsVars> {
  @override
  final Iterable<Type> types = const [GqueryAssetsVars, _$GqueryAssetsVars];
  @override
  final String wireName = 'GqueryAssetsVars';

  @override
  Iterable<Object?> serialize(Serializers serializers, GqueryAssetsVars object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      'value',
      serializers.serialize(object.value,
          specifiedType: const FullType(String)),
    ];
    Object? value;
    value = object.pageNumber;
    if (value != null) {
      result
        ..add('pageNumber')
        ..add(serializers.serialize(value, specifiedType: const FullType(int)));
    }
    return result;
  }

  @override
  GqueryAssetsVars deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GqueryAssetsVarsBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case 'value':
          result.value = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
        case 'pageNumber':
          result.pageNumber = serializers.deserialize(value,
              specifiedType: const FullType(int)) as int?;
          break;
      }
    }

    return result.build();
  }
}

class _$GqueryAssetsVars extends GqueryAssetsVars {
  @override
  final String value;
  @override
  final int? pageNumber;

  factory _$GqueryAssetsVars(
          [void Function(GqueryAssetsVarsBuilder)? updates]) =>
      (new GqueryAssetsVarsBuilder()..update(updates))._build();

  _$GqueryAssetsVars._({required this.value, this.pageNumber}) : super._() {
    BuiltValueNullFieldError.checkNotNull(value, r'GqueryAssetsVars', 'value');
  }

  @override
  GqueryAssetsVars rebuild(void Function(GqueryAssetsVarsBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GqueryAssetsVarsBuilder toBuilder() =>
      new GqueryAssetsVarsBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GqueryAssetsVars &&
        value == other.value &&
        pageNumber == other.pageNumber;
  }

  @override
  int get hashCode {
    return $jf($jc($jc(0, value.hashCode), pageNumber.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GqueryAssetsVars')
          ..add('value', value)
          ..add('pageNumber', pageNumber))
        .toString();
  }
}

class GqueryAssetsVarsBuilder
    implements Builder<GqueryAssetsVars, GqueryAssetsVarsBuilder> {
  _$GqueryAssetsVars? _$v;

  String? _value;
  String? get value => _$this._value;
  set value(String? value) => _$this._value = value;

  int? _pageNumber;
  int? get pageNumber => _$this._pageNumber;
  set pageNumber(int? pageNumber) => _$this._pageNumber = pageNumber;

  GqueryAssetsVarsBuilder();

  GqueryAssetsVarsBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _value = $v.value;
      _pageNumber = $v.pageNumber;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GqueryAssetsVars other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GqueryAssetsVars;
  }

  @override
  void update(void Function(GqueryAssetsVarsBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GqueryAssetsVars build() => _build();

  _$GqueryAssetsVars _build() {
    final _$result = _$v ??
        new _$GqueryAssetsVars._(
            value: BuiltValueNullFieldError.checkNotNull(
                value, r'GqueryAssetsVars', 'value'),
            pageNumber: pageNumber);
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: always_put_control_body_on_new_line,always_specify_types,annotate_overrides,avoid_annotating_with_dynamic,avoid_as,avoid_catches_without_on_clauses,avoid_returning_this,deprecated_member_use_from_same_package,lines_longer_than_80_chars,no_leading_underscores_for_local_identifiers,omit_local_variable_types,prefer_expression_function_bodies,sort_constructors_first,test_types_in_equals,unnecessary_const,unnecessary_new,unnecessary_lambdas
