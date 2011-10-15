-- Fix Type in Players
ALTER TABLE players
CHANGE `advenced_stigma_slot_size` `advanced_stigma_slot_size` tinyint(1) NOT NULL DEFAULT '0';
