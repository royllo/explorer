<template>
  <!-- ============================================================================================================= -->
  <!-- Default layout.                                                                                               -->
  <!-- ============================================================================================================= -->

  <div class="flex flex-col h-screen justify-between">

    <!-- Our header -->
    <DefaultHeader/>

    <!-- Content -->
    <slot/>

    <!-- Our footer -->
    <DefaultFooter/>

  </div>

</template>