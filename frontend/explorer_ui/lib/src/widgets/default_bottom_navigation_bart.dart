import 'package:flutter/material.dart';

// Default Royllo bottom navigation bar.
Widget defaultRoylloBottomNavigationBar() {
  // Return component.
  return BottomNavigationBar(items: const <BottomNavigationBarItem>[
    BottomNavigationBarItem(
      icon: Icon(Icons.home),
      label: 'Home',
    ),
    BottomNavigationBarItem(
      icon: Icon(Icons.help),
      label: 'About',
    ),
  ]);
}
