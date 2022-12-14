// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'getUser.var.gql.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

Serializer<GuserByUsernameVars> _$guserByUsernameVarsSerializer =
    new _$GuserByUsernameVarsSerializer();

class _$GuserByUsernameVarsSerializer
    implements StructuredSerializer<GuserByUsernameVars> {
  @override
  final Iterable<Type> types = const [
    GuserByUsernameVars,
    _$GuserByUsernameVars
  ];
  @override
  final String wireName = 'GuserByUsernameVars';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GuserByUsernameVars object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      'username',
      serializers.serialize(object.username,
          specifiedType: const FullType(String)),
    ];

    return result;
  }

  @override
  GuserByUsernameVars deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GuserByUsernameVarsBuilder();

    final iterator = serialized.iterator;
    while (iterator.moveNext()) {
      final key = iterator.current! as String;
      iterator.moveNext();
      final Object? value = iterator.current;
      switch (key) {
        case 'username':
          result.username = serializers.deserialize(value,
              specifiedType: const FullType(String))! as String;
          break;
      }
    }

    return result.build();
  }
}

class _$GuserByUsernameVars extends GuserByUsernameVars {
  @override
  final String username;

  factory _$GuserByUsernameVars(
          [void Function(GuserByUsernameVarsBuilder)? updates]) =>
      (new GuserByUsernameVarsBuilder()..update(updates))._build();

  _$GuserByUsernameVars._({required this.username}) : super._() {
    BuiltValueNullFieldError.checkNotNull(
        username, r'GuserByUsernameVars', 'username');
  }

  @override
  GuserByUsernameVars rebuild(
          void Function(GuserByUsernameVarsBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GuserByUsernameVarsBuilder toBuilder() =>
      new GuserByUsernameVarsBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GuserByUsernameVars && username == other.username;
  }

  @override
  int get hashCode {
    return $jf($jc(0, username.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GuserByUsernameVars')
          ..add('username', username))
        .toString();
  }
}

class GuserByUsernameVarsBuilder
    implements Builder<GuserByUsernameVars, GuserByUsernameVarsBuilder> {
  _$GuserByUsernameVars? _$v;

  String? _username;
  String? get username => _$this._username;
  set username(String? username) => _$this._username = username;

  GuserByUsernameVarsBuilder();

  GuserByUsernameVarsBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _username = $v.username;
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GuserByUsernameVars other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GuserByUsernameVars;
  }

  @override
  void update(void Function(GuserByUsernameVarsBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GuserByUsernameVars build() => _build();

  _$GuserByUsernameVars _build() {
    final _$result = _$v ??
        new _$GuserByUsernameVars._(
            username: BuiltValueNullFieldError.checkNotNull(
                username, r'GuserByUsernameVars', 'username'));
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: always_put_control_body_on_new_line,always_specify_types,annotate_overrides,avoid_annotating_with_dynamic,avoid_as,avoid_catches_without_on_clauses,avoid_returning_this,deprecated_member_use_from_same_package,lines_longer_than_80_chars,no_leading_underscores_for_local_identifiers,omit_local_variable_types,prefer_expression_function_bodies,sort_constructors_first,test_types_in_equals,unnecessary_const,unnecessary_new,unnecessary_lambdas
