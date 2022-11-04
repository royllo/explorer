<template>
  <!-- ============================================================================================================= -->
  <!-- Search engine screen                                                                                          -->
  <!-- ============================================================================================================= -->
  <!--
        CSS explanation:
          - flex: The Flexbox module aims at providing a more efficient way to lay out, align and distribute space among
          items in a container, even when their size is unknown and/or dynamic.
          - flex-col to position flex items vertically.
          - h-screen to make an element span the entire height of the viewport (Usefull to create a footer).
          - justify-between to justify items along the container’s main axis such that there is an equal amount of space
          between each item.
          - the mx-auto is used to center a container.
          - w-full to set an element to a percentage based width.
          - sr-only to hide an element visually without hiding it from screen readers:
  -->
  <div class="flex flex-col h-screen mt-11">

    <!-- Logo (The mx-auto should also go to the image as tailwind make the image a block element) -->
    <a href="/">
      <img class="mx-auto"
           src="/royllo_logo_homepage.png"
           alt="Royllo logo"/>
    </a>

    <!-- Search form -->
    <SearchAssetForm/>

  </div>

</template>
