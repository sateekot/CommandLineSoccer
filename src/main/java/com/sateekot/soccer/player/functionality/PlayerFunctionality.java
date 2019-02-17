package com.sateekot.soccer.player.functionality;

/**
 * 
 * @author sateekot
 * Provides the operations fof players
 */
public interface PlayerFunctionality {

	default void play() {}
	default void defend() {}
	default void forward() {}
	default String shoot() {
		return "default";}
	default boolean save(String userOption, String computeroption) {
		return false;}
	default void block() {}
}
