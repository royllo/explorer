import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

// Default Royllo bottom navigation bar.
Widget defaultRoylloBottomNavigationBar(BuildContext context) {
  // Return component.
  return BottomNavigationBar(items: <BottomNavigationBarItem>[
    BottomNavigationBarItem(
      icon: IconButton(
        icon: const Icon(Icons.home),
        onPressed: () => GoRouter.of(context).goNamed("home"),
      ),
      label: 'Home',
    ),
    BottomNavigationBarItem(
      icon: IconButton(
        icon: const Icon(Icons.help),
        onPressed: () => GoRouter.of(context).goNamed("about"),
      ),
      label: 'About',
    ),
  ]);
}
