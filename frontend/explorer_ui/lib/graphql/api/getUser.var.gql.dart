// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint

// ignore_for_file: no_leading_underscores_for_library_prefixes
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:explorer_ui/graphql/api/serializers.gql.dart' as _i1;

part 'getUser.var.gql.g.dart';

abstract class GuserByUsernameVars
    implements Built<GuserByUsernameVars, GuserByUsernameVarsBuilder> {
  GuserByUsernameVars._();

  factory GuserByUsernameVars(
      [Function(GuserByUsernameVarsBuilder b) updates]) = _$GuserByUsernameVars;

  String get username;
  static Serializer<GuserByUsernameVars> get serializer =>
      _$guserByUsernameVarsSerializer;
  Map<String, dynamic> toJson() => (_i1.serializers.serializeWith(
        GuserByUsernameVars.serializer,
        this,
      ) as Map<String, dynamic>);
  static GuserByUsernameVars? fromJson(Map<String, dynamic> json) =>
      _i1.serializers.deserializeWith(
        GuserByUsernameVars.serializer,
        json,
      );
}
