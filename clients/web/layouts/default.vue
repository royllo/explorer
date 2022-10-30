<script setup>
// Current year used to write the copyright year.
const currentYear = new Date().getFullYear();
</script>

<template>
  <div class="flex flex-col h-screen justify-between">

    <DefaultHeader/>

    <!--
        ================================================================================================================
        Using a container: a component used for fixing an element's width.
        md:container is a responsive variant allowing you to make something behave like a container at only a certain
        breakpoint and up.
        Tailwind’s container does not center itself automatically, to do it, we use the mx-auto.
        ================================================================================================================
    -->
    <main class="md:container md:mx-auto">
      <slot/>
    </main>

    <!--
        ================================================================================================================
        Footer
        ================================================================================================================
    -->
    <footer class="p-4 bg-gray-100 rounded-lg shadow md:flex md:items-center md:justify-between md:p-6 dark:bg-gray-800">
      <!-- Left part : Royllo copyright and link -->
      <span class="text-sm text-gray-500 sm:text-center dark:text-gray-400">
      © {{ currentYear }} <a href="https://www.royllo.org"
                             class="hover:underline"
                             target="royllo_website">Royllo</a>. All Rights Reserved.
      </span>
      <!-- Right part : Links to know more about Royllo -->
      <ul class="flex flex-wrap items-center mt-3 text-sm text-gray-500 dark:text-gray-400 sm:mt-0">
        <li>
          <a href="#" class="mr-4 hover:underline md:mr-6 ">About</a>
        </li>
        <li>
          <a href="https://github.com/royllo/explorer" class="mr-4 hover:underline md:mr-6" target="github">Github</a>
        </li>
        <li>
          <a href="https://twitter.com/royllo_systems" class="mr-4 hover:underline md:mr-6" target="twitter">Twitter</a>
        </li>
        <li>
          <a href="mailto:contact@royllo.org" class="hover:underline">Contact</a>
        </li>
      </ul>
    </footer>

  </div>
</template>