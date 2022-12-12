// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i1;

part 'getUser.data.gql.g.dart';

abstract class GuserByUsernameData
    implements Built<GuserByUsernameData, GuserByUsernameDataBuilder> {
  GuserByUsernameData._();

  factory GuserByUsernameData(
      [Function(GuserByUsernameDataBuilder b) updates]) = _$GuserByUsernameData;

  static void _initializeBuilder(GuserByUsernameDataBuilder b) =>
      b..G__typename = 'Query';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  GuserByUsernameData_userByUsername? get userByUsername;
  static Serializer<GuserByUsernameData> get serializer =>
      _$guserByUsernameDataSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GuserByUsernameData.serializer,
        this,
      ) as Map<String, dynamic>);
  static GuserByUsernameData? fromJson(Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GuserByUsernameData.serializer,
        json,
      );
}

abstract class GuserByUsernameData_userByUsername
    implements
        Built<GuserByUsernameData_userByUsername,
            GuserByUsernameData_userByUsernameBuilder> {
  GuserByUsernameData_userByUsername._();

  factory GuserByUsernameData_userByUsername(
          [Function(GuserByUsernameData_userByUsernameBuilder b) updates]) =
      _$GuserByUsernameData_userByUsername;

  static void _initializeBuilder(GuserByUsernameData_userByUsernameBuilder b) =>
      b..G__typename = 'User';
  @BuiltValueField(wireName: '__typename')
  String get G__typename;
  String get id;
  String get username;
  static Serializer<GuserByUsernameData_userByUsername> get serializer =>
      _$guserByUsernameDataUserByUsernameSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GuserByUsernameData_userByUsername.serializer,
        this,
      ) as Map<String, dynamic>);
  static GuserByUsernameData_userByUsername? fromJson(
          Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GuserByUsernameData_userByUsername.serializer,
        json,
      );
}
