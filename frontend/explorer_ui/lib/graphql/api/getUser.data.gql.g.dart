// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'getUser.data.gql.dart';

// **************************************************************************
// BuiltValueGenerator
// **************************************************************************

Serializer<GuserByUsernameData> _$guserByUsernameDataSerializer =
    new _$GuserByUsernameDataSerializer();
Serializer<GuserByUsernameData_userByUsername>
    _$guserByUsernameDataUserByUsernameSerializer =
    new _$GuserByUsernameData_userByUsernameSerializer();

class _$GuserByUsernameDataSerializer
    implements StructuredSerializer<GuserByUsernameData> {
  @override
  final Iterable<Type> types = const [
    GuserByUsernameData,
    _$GuserByUsernameData
  ];
  @override
  final String wireName = 'GuserByUsernameData';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GuserByUsernameData object,
      {FullType specifiedType = FullType.unspecified}) {
    final result = <Object?>[
      '__typename',
      serializers.serialize(object.G__typename,
          specifiedType: const FullType(String)),
    ];
    Object? value;
    value = object.userByUsername;
    if (value != null) {
      result
        ..add('userByUsername')
        ..add(serializers.serialize(value,
            specifiedType: const FullType(GuserByUsernameData_userByUsername)));
    }
    return result;
  }

  @override
  GuserByUsernameData deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GuserByUsernameDataBuilder();

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
        case 'userByUsername':
          result.userByUsername.replace(serializers.deserialize(value,
                  specifiedType:
                      const FullType(GuserByUsernameData_userByUsername))!
              as GuserByUsernameData_userByUsername);
          break;
      }
    }

    return result.build();
  }
}

class _$GuserByUsernameData_userByUsernameSerializer
    implements StructuredSerializer<GuserByUsernameData_userByUsername> {
  @override
  final Iterable<Type> types = const [
    GuserByUsernameData_userByUsername,
    _$GuserByUsernameData_userByUsername
  ];
  @override
  final String wireName = 'GuserByUsernameData_userByUsername';

  @override
  Iterable<Object?> serialize(
      Serializers serializers, GuserByUsernameData_userByUsername object,
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
  GuserByUsernameData_userByUsername deserialize(
      Serializers serializers, Iterable<Object?> serialized,
      {FullType specifiedType = FullType.unspecified}) {
    final result = new GuserByUsernameData_userByUsernameBuilder();

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

class _$GuserByUsernameData extends GuserByUsernameData {
  @override
  final String G__typename;
  @override
  final GuserByUsernameData_userByUsername? userByUsername;

  factory _$GuserByUsernameData(
          [void Function(GuserByUsernameDataBuilder)? updates]) =>
      (new GuserByUsernameDataBuilder()..update(updates))._build();

  _$GuserByUsernameData._({required this.G__typename, this.userByUsername})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        G__typename, r'GuserByUsernameData', 'G__typename');
  }

  @override
  GuserByUsernameData rebuild(
          void Function(GuserByUsernameDataBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GuserByUsernameDataBuilder toBuilder() =>
      new GuserByUsernameDataBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GuserByUsernameData &&
        G__typename == other.G__typename &&
        userByUsername == other.userByUsername;
  }

  @override
  int get hashCode {
    return $jf($jc($jc(0, G__typename.hashCode), userByUsername.hashCode));
  }

  @override
  String toString() {
    return (newBuiltValueToStringHelper(r'GuserByUsernameData')
          ..add('G__typename', G__typename)
          ..add('userByUsername', userByUsername))
        .toString();
  }
}

class GuserByUsernameDataBuilder
    implements Builder<GuserByUsernameData, GuserByUsernameDataBuilder> {
  _$GuserByUsernameData? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  GuserByUsernameData_userByUsernameBuilder? _userByUsername;
  GuserByUsernameData_userByUsernameBuilder get userByUsername =>
      _$this._userByUsername ??=
          new GuserByUsernameData_userByUsernameBuilder();
  set userByUsername(
          GuserByUsernameData_userByUsernameBuilder? userByUsername) =>
      _$this._userByUsername = userByUsername;

  GuserByUsernameDataBuilder() {
    GuserByUsernameData._initializeBuilder(this);
  }

  GuserByUsernameDataBuilder get _$this {
    final $v = _$v;
    if ($v != null) {
      _G__typename = $v.G__typename;
      _userByUsername = $v.userByUsername?.toBuilder();
      _$v = null;
    }
    return this;
  }

  @override
  void replace(GuserByUsernameData other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GuserByUsernameData;
  }

  @override
  void update(void Function(GuserByUsernameDataBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GuserByUsernameData build() => _build();

  _$GuserByUsernameData _build() {
    _$GuserByUsernameData _$result;
    try {
      _$result = _$v ??
          new _$GuserByUsernameData._(
              G__typename: BuiltValueNullFieldError.checkNotNull(
                  G__typename, r'GuserByUsernameData', 'G__typename'),
              userByUsername: _userByUsername?.build());
    } catch (_) {
      late String _$failedField;
      try {
        _$failedField = 'userByUsername';
        _userByUsername?.build();
      } catch (e) {
        throw new BuiltValueNestedFieldError(
            r'GuserByUsernameData', _$failedField, e.toString());
      }
      rethrow;
    }
    replace(_$result);
    return _$result;
  }
}

class _$GuserByUsernameData_userByUsername
    extends GuserByUsernameData_userByUsername {
  @override
  final String G__typename;
  @override
  final String id;
  @override
  final String username;

  factory _$GuserByUsernameData_userByUsername(
          [void Function(GuserByUsernameData_userByUsernameBuilder)?
              updates]) =>
      (new GuserByUsernameData_userByUsernameBuilder()..update(updates))
          ._build();

  _$GuserByUsernameData_userByUsername._(
      {required this.G__typename, required this.id, required this.username})
      : super._() {
    BuiltValueNullFieldError.checkNotNull(
        G__typename, r'GuserByUsernameData_userByUsername', 'G__typename');
    BuiltValueNullFieldError.checkNotNull(
        id, r'GuserByUsernameData_userByUsername', 'id');
    BuiltValueNullFieldError.checkNotNull(
        username, r'GuserByUsernameData_userByUsername', 'username');
  }

  @override
  GuserByUsernameData_userByUsername rebuild(
          void Function(GuserByUsernameData_userByUsernameBuilder) updates) =>
      (toBuilder()..update(updates)).build();

  @override
  GuserByUsernameData_userByUsernameBuilder toBuilder() =>
      new GuserByUsernameData_userByUsernameBuilder()..replace(this);

  @override
  bool operator ==(Object other) {
    if (identical(other, this)) return true;
    return other is GuserByUsernameData_userByUsername &&
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
    return (newBuiltValueToStringHelper(r'GuserByUsernameData_userByUsername')
          ..add('G__typename', G__typename)
          ..add('id', id)
          ..add('username', username))
        .toString();
  }
}

class GuserByUsernameData_userByUsernameBuilder
    implements
        Builder<GuserByUsernameData_userByUsername,
            GuserByUsernameData_userByUsernameBuilder> {
  _$GuserByUsernameData_userByUsername? _$v;

  String? _G__typename;
  String? get G__typename => _$this._G__typename;
  set G__typename(String? G__typename) => _$this._G__typename = G__typename;

  String? _id;
  String? get id => _$this._id;
  set id(String? id) => _$this._id = id;

  String? _username;
  String? get username => _$this._username;
  set username(String? username) => _$this._username = username;

  GuserByUsernameData_userByUsernameBuilder() {
    GuserByUsernameData_userByUsername._initializeBuilder(this);
  }

  GuserByUsernameData_userByUsernameBuilder get _$this {
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
  void replace(GuserByUsernameData_userByUsername other) {
    ArgumentError.checkNotNull(other, 'other');
    _$v = other as _$GuserByUsernameData_userByUsername;
  }

  @override
  void update(
      void Function(GuserByUsernameData_userByUsernameBuilder)? updates) {
    if (updates != null) updates(this);
  }

  @override
  GuserByUsernameData_userByUsername build() => _build();

  _$GuserByUsernameData_userByUsername _build() {
    final _$result = _$v ??
        new _$GuserByUsernameData_userByUsername._(
            G__typename: BuiltValueNullFieldError.checkNotNull(G__typename,
                r'GuserByUsernameData_userByUsername', 'G__typename'),
            id: BuiltValueNullFieldError.checkNotNull(
                id, r'GuserByUsernameData_userByUsername', 'id'),
            username: BuiltValueNullFieldError.checkNotNull(
                username, r'GuserByUsernameData_userByUsername', 'username'));
    replace(_$result);
    return _$result;
  }
}

// ignore_for_file: always_put_control_body_on_new_line,always_specify_types,annotate_overrides,avoid_annotating_with_dynamic,avoid_as,avoid_catches_without_on_clauses,avoid_returning_this,deprecated_member_use_from_same_package,lines_longer_than_80_chars,no_leading_underscores_for_local_identifiers,omit_local_variable_types,prefer_expression_function_bodies,sort_constructors_first,test_types_in_equals,unnecessary_const,unnecessary_new,unnecessary_lambdas
