<template>
  <div class="flex flex-col h-screen justify-between">

    <!-- Our header -->
    <defaultHeader/>

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

    <!-- Our header -->
    <defaultFooter/>

  </div>
</template>