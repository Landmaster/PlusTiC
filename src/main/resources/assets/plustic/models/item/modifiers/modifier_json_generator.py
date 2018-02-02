#!/usr/bin/env python3
for modifierSpec in [
	('bane_of_arthopods', 'bane_spider'), # Why TConstruct…
	('beheading', 'beheading'),
	('diamond', 'diamond'),
	('emerald', 'emerald'),
	('glowing', 'glowing'),
	('haste', 'haste'),
	('knockback', 'knockback'),
	('luck', 'luck'),
	('mending_moss', 'mending_moss'),
	('necrotic', 'necrotic'),
	('reinforced', 'reinforced'),
	('sharpness', 'sharpness'),
	('shulking', 'shulking'),
	('silktouch', 'silk'),
	('smite', 'smite'),
	('soulbound', 'soulbound'),
	('webbed', 'web'),
	]:
	with open(modifierSpec[0]+'.json', 'w', encoding='utf-8') as file:
		file.write('''{
\t"textures": {
\t\t"katana": "plustic:items/katana/mod_%(shortName)s",
\t\t"laser_gun": "plustic:items/laser_gun/mod_%(shortName)s"
\t}
}''' % { 'shortName': modifierSpec[1] })