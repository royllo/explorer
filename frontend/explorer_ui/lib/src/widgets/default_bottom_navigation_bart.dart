import 'package:explorer_ui/src/configuration/routes.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

// Default Royllo bottom navigation bar.
Widget defaultRoylloBottomNavigationBar(BuildContext context) {
  // Return component.
  return BottomNavigationBar(items: <BottomNavigationBarItem>[
    // =========================================================================
    // Menu "Home"
    BottomNavigationBarItem(
      icon: IconButton(
        icon: const Icon(Icons.home),
        onPressed: () => GoRouter.of(context).goNamed(homeRouteName),
      ),
      label: 'Home',
    ),
    // =========================================================================
    // Menu "About"
    BottomNavigationBarItem(
      icon: IconButton(
        icon: const Icon(Icons.help),
        onPressed: () => GoRouter.of(context).goNamed(aboutRouteName),
      ),
      label: 'About',
    ),
  ]);
}
