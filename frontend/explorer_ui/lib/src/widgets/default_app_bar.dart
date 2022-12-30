import 'package:explorer_ui/src/configuration/routes.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

// Default Royllo app bar.
PreferredSizeWidget defaultRoylloAppBar(BuildContext context) {
  // Return component.
  return AppBar(
    leading: IconButton(
      icon: const Icon(Icons.home),
      onPressed: () => GoRouter.of(context).goNamed(homeRouteName),
    ),
  );
}
