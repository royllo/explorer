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

    <!-- Search field -->
    <form class="flex mx-auto">
      <label for="simple-search" class="sr-only">Search asset by asset id or asset name</label>
      <div class="relative w-96">
        <div class="flex absolute inset-y-0 left-0 items-center pl-3 pointer-events-none">
          <svg aria-hidden="true" class="w-5 h-5 text-gray-500 dark:text-gray-400" fill="currentColor"
               viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd"
                  d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                  clip-rule="evenodd"></path>
          </svg>
        </div>
        <input type="search" id="simple-search"
               class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
               placeholder="Search asset by asset id or asset name"
               autofocus
               required>
      </div>
      <button type="submit"
              class="p-2.5 ml-2 text-sm font-medium text-white bg-blue-700 rounded-lg border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
        </svg>
        <span class="sr-only">Search</span>
      </button>
    </form>

  </div>

</template>
