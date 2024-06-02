package it.brokenengineers.dnd_character_manager.test_utils

import androidx.navigation.NavController
import org.junit.Assert

fun NavController.assertCurrentRouteEqual(expectedRouteName: String) {
    val currentRoute = currentBackStackEntry?.destination?.route.toString()
    Assert.assertEquals(expectedRouteName, currentRoute)
}

fun NavController.assertCurrentRouteWithIdEqual(expectedRouteName: String) {
    val id = currentBackStackEntry?.arguments?.getInt("characterId")
    val currentRoute = currentBackStackEntry?.destination?.route
        .toString().replace("{characterId}", id.toString())
    Assert.assertEquals(expectedRouteName, currentRoute)
}

fun NavController.assertCurrentRouteContains(partialRoute: String) {
    val currentRoute = currentBackStackEntry?.destination?.route.toString()
    Assert.assertTrue(currentRoute.contains(partialRoute))
}