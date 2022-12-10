import 'package:flutter/material.dart';

// Default Royllo app bar.
PreferredSizeWidget defaultRoylloAppBar() {
  // Return component.
  return AppBar(
    leading: IconButton(
      // TODO Add a link to come back to the homepage.
      onPressed: () {},
      icon: const Icon(Icons.home),
    ),
  );
}
