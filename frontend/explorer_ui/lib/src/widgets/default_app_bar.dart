import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

// Default Royllo app bar.
PreferredSizeWidget defaultRoylloAppBar(BuildContext context) {
  // Return component.
  return AppBar(
    leading: IconButton(
      // TODO Add a link to come back to the homepage.
      onPressed: () => GoRouter.of(context).goNamed("home"),
      icon: const Icon(Icons.home),
    ),
  );
}
