DROP TABLE IF EXISTS news;
CREATE TABLE news
(
    id BIGSERIAL PRIMARY KEY,
    time TIME NOT NULL,
    title VARCHAR(255) NOT NULL UNIQUE,
    text TEXT NOT NULL
);

DROP TABLE IF EXISTS comments;
CREATE TABLE comments
(
    id BIGSERIAL PRIMARY KEY,
    time TIME NOT NULL,
    text TEXT NOT NULL,
    username VARCHAR(50),
    news_id INT NOT NULL,
    FOREIGN KEY (news_id) REFERENCES news(id)
);

INSERT INTO news VALUES (1, '08:15:00', 'The secret of the presidential cue card', 'Los Angeles Times reporter Courtney Subramanian’s question did turn out to be on a basically similar issue to the one predicted on Biden’s card – about an apparent conflict between US efforts to slow semiconductor manufacturing in China and the trading interests of its allies. But the query did not exactly match the one Biden was apparently expecting and the LA Times said in a statement that Subramanian did not submit her question to the press office in advance, though she had been in regular contact with White House aides seeking information for her reporting.');
INSERT INTO news VALUES (2, '14:35:10', 'Discord leaker. Catch up on the new allegations', 'CNN — It’s been two weeks since 21-year-old Jack Teixeira was arrested in shorts in front of his mother’s house, accused of leaking a trove of classified documents over a period of months to a closed online community on Discord, a social media platform popular with video gamers.');
INSERT INTO news VALUES (3, '13:07:46', 'States are moving in polar opposite directions on guns', 'CNN - With almost all gun legislation a nonstarter on a divided Capitol Hill, states are charting wildly divergent paths. Some states are doing everything they can to restrict access to certain weapons. Others are doing everything they can to make guns as accessible as possible without much government oversight. On the restriction side, Washington on Tuesday became the 10th state to impose an assault-weapons ban, signaling new movement among states to step in where the federal government has failed. The national assault-weapons ban, which was in place for 10 years, lapsed in 2004.');
INSERT INTO news VALUES (4, '02:43:01', 'Election 2024. A consequential rematch of serial candidates', 'CNN — This could be the most consequential rematch in US history – a president who portrays himself as the champion of democracy against a former president who routinely rejects election results. Anyone fearing Donald Trump’s return and those who reject Joe Biden’s victory could agree that nothing less than the future of the republic is at stake if Biden and Trump are on the ballot in 2024. Biden explicitly made that case in his announcement video on Tuesday, saying “we still are” in a “battle for the soul of America.”');
INSERT INTO news VALUES (5, '21:14:46', 'McCarthy’s victory on debt ceiling vote may be short-lived', 'CNN — Speaker Kevin McCarthy passed his toughest test yet by buying off holdout Republicans and squeezing a bill with huge spending cuts through the House on Wednesday. But the price of proving his authority with a radical GOP conference was pushing the nation closer to a debt default cliff edge in an increasingly alarming showdown with President Joe Biden. The bill, which raises the debt ceiling for a year in return for spending cuts, was designed to hike pressure on Biden, who is refusing to accept Republican conditions that would effectively gut his domestic agenda. Unless the debt ceiling is lifted by the summer, the economy could crash.');
INSERT INTO news VALUES (6, '15:08:22', 'Billionaire GOP donor says DeSantis won''t return his calls', 'John Catsimatidis, the billionaire CEO of the Gristedes supermarket chain who donated to Donald Trump''s 2020 re-election campaign, says that Florida Gov. Ron DeSantis isn''t returning his calls despite DeSantis eying a 2024 presidential run.');
INSERT INTO news VALUES (7, '08:26:02', 'We are already in the food fight portion of the GOP primary', 'CNN — The 2024 Republican presidential primary is not fully underway as yet and already we are in the food fight phase. A super PAC supporting former President Donald Trump tried to smear Florida Gov. Ron DeSantis with pudding, seizing on a report, which the governor denies, about his eating habits to make a point about Social Security and Medicare. The ad itself is gross. And it drew a super PAC supporting DeSantis off the sidelines to air an ad of its own wondering why Trump was going after the Florida governor.');
INSERT INTO news VALUES (8, '18:24:12', 'Sexual assaults in the US military increased by 1% last year', 'CNN — The US military saw a 1% increase in sexual assaults last year, according to the Pentagon’s latest annual report. There were 7,378 reports of sexual assault against service members in 2022, according to the Fiscal Year 2022 Annual Report on Sexual Assault in the Military, released on Thursday. That is up from 7,260 reports of assault in 2021. All of the services aside from the Army saw an increase in reports from last year, officials said during a briefing on the report on Thursday. the Navy, Marine Corps, and Air Force saw a 9%, 3.6%, and 13% increase in reports, respectively. The Army, meanwhile, saw a 9% decrease.');
INSERT INTO news VALUES (9, '21:18:02', 'US Army renames Fort Lee after 2 pioneering Black Army officers', 'CNN  — The US Army renamed Virginia’s Fort Lee military base Thursday in honor of two Black service members, following a branch-wide push to rename bases named after Confederate leaders. The base will be “redesignated” as Fort Gregg-Adams after Lt. Gen. Arthur Gregg and Lt. Col. Charity Adams, the Army announced on Twitter. It had been named after Gen. Robert E. Lee, who led Confederate forces in the American Civil War. “Both Soldiers excelled in the field of sustainment and made significant contributions to USArmy history, the Army said in a tweet. We’re proud to honor the courage, sacrifice, and diversity of these distinguished Soldiers and also our civilians.');
INSERT INTO news VALUES (10, '17:31:16', 'Ukraine war. Russia launches second pre-dawn missile attack in three days', 'Russia has launched a series of missiles at Ukrainian cities in the second pre-dawn attack in three days. Pavlohrad, a logistics hub near the central city of Dnipro, was hit ahead of a much-anticipated counter-offensive by Ukraine. The strike sparked a major fire, destroyed dozens of houses, and wounded 34 people. Hours later, the air raid alert sounded across the country, with the capital Kyiv among the targets. Across the country, the Ukrainian army said it shot down 15 of the 18 cruise missiles that had been fired.');
INSERT INTO news VALUES (11, '02:08:13', 'First Republic. JP Morgan to take over major US bank', 'JP Morgan Chase is set to take over the troubled US bank First Republic in a deal brokered by regulators. The Federal Deposit Insurance Corporation (FDIC) confirmed in a statement that First Republic Bank had collapsed on Monday. Investment banking giant JP Morgan will take on all of the deposits and the majority of First Republic''s assets. First Republic is the third US bank to collapse in recent months, which has prompted fears of wider banking crisis. The San Francisco-based lender''s shares fell by more than 75% last week after it admitted that customers had withdrawn $100bn (£79.6bn) of deposits in March.');
INSERT INTO news VALUES (12, '01:16:39', 'Sudan crisis Chaos at port as thousands rush to leave', 'In the dead of night, as HMS Al Diriyah approached Sudan''s coast, Saudi officers flicked on sweeping search lights to secure safe passage for their warship into a harbour rapidly transforming into a major evacuation and humanitarian hub in Sudan''s deepening crisis. Even at 2am two other hulking vessels were also anchored offshore at Port Sudan, its largest port, waiting their turn in this international rescue effort."I feel so relieved but also so sad to be part of this history," Hassan Faraz from Pakistan told us, visibly shaken.We reached the quayside in a Saudi tugboat at the end of a 10-hour journey through the night in HMS Al Diriyah from the Saudi port city of Jeddah. A small group of foreign journalists were given rare access to enter embattled Sudan, if only briefly.');
INSERT INTO news VALUES (13, '05:49:36', 'Explosion in Russian border region derails freight train - governor', 'An explosion in the Russian border region of Bryansk derailed a freight train on Monday, authorities said. Local governor Alexander Bogomaz said an explosive device went off along the Bryansk-Unecha line, 60km from Ukraine. The incident, which occurred at 10:17 Moscow time (07:17 GMT), saw the locomotive catch fire and seven freight wagons derailed, Russian Railways said. The region - which borders Ukraine and Belarus - has seen acts of sabotage since Russia invaded Ukraine.');
INSERT INTO news VALUES (14, '12:10:23', 'Jock Zonfrillo. MasterChef Australia host dies suddenly, aged 46', 'Award-winning chef and MasterChef Australia host Jock Zonfrillo has died suddenly, aged 46. The Scotsman worked in renowned restaurants around the world before opening his own in Australia. His death was confirmed by broadcaster Network 10 on the day the 2023 season premiere of MasterChef was set to air. Zonfrillo is survived by his wife Lauren Fried and four children, who said in a statement their hearts were "shattered". "For those who crossed his path, became his mate, or were lucky enough to be his family, keep this proud Scot in your hearts when you have your next whisky," the family said.');
INSERT INTO news VALUES (15, '16:30:10', 'Suspected Islamic State chief Qurayshi killed in Syria, Turkey says', 'Turkish forces have killed the suspected leader of the Islamic State (IS) group in Syria, Turkey''s President Recep Tayyip Erdogan has announced. Abu Hussein al-Qurayshi is said to have taken over the group after his predecessor was killed last autumn. Mr Erdogan told broadcaster TRT Turk the IS leader was "neutralised" in a Turkish MIT intelligence agency operation on Saturday. IS has so far made no comment on the reported operation.');
INSERT INTO news VALUES (16, '10:01:27', 'Maurizio Cattelan. Banana artwork eaten by Seoul museum visitor', 'A South Korean art student ate a banana that was part of an installation by artist Maurizio Cattelan, saying he was "hungry" after skipping breakfast. The artwork called "Comedian", part of Cattelan''s''s exhibition "WE", consisted of a ripe banana duct-taped to a wall at Seoul''s Leeum Musuem of Art. After eating the banana, the student, Noh Huyn-soo, taped the peel to the wall. The museum later placed a new banana at the same spot, reported local media. The incident, which lasted more than a minute, was recorded by Mr Noh''s friend.');
INSERT INTO news VALUES (17, '01:04:30', 'Shaquil Barretts daughter, aged two, drowns in pool at Florida home', 'The two-year-old daughter of a Tampa Bay Buccaneers NFL player drowned in a swimming pool at the family''s home on Sunday, authorities said. Police officers responded to a call that Arrayah, the youngest daughter of Shaquil Barrett, fell into the pool around 09:30 local time (14:30 BST). She was taken to a hospital in Tampa, Florida, and pronounced dead. A Super Bowl winning linebacker with the Buccaneers, Mr Barrett, 30, and his wife have three other children. The Buccaneers released a statement on the "tragic" and "heartbreaking" news.');
INSERT INTO news VALUES (18, '05:26:50', 'Boss of Mexico''s migration authority charged over deadly fire', 'The head of Mexico''s migration authority, Francisco Garduño, has been charged in connection with a deadly fire at a migrant centre in Ciudad Juárez on 27 March. Forty migrants from Central and South America died behind bars when smoke filled a cell they were being held in. CCTV footage appeared to show the guards walking away as the fire spread without opening the locked cell door. Mr Garduño is the most senior official to be charged over the fire.');
INSERT INTO news VALUES (19, '13:04:02', 'Climate change. Spain breaks record temperature for April', 'The record figure was reached in Cordoba airport in southern Spain just after 15:00 local time (14:00 BST). For days a blistering heatwave has hit the country with temperatures 10-15C warmer than expected for April. Its been driven by a mass of very hot air from Africa, coupled with a slow moving weather system. "This is not normal. Temperatures are completely out of control this year," Cayetano Torres, a spokesman for Spain''s meteorological office, told BBC News.');
INSERT INTO news VALUES (20, '19:10:58', 'BP faces green protest over new climate goals', 'Some of the UK''s biggest pension funds have voted against reappointing BP''s chairman over a decision to weaken its climate plans, but the majority of shareholders backed Helge Lund. It comes after the energy giant cut back its target to reduce emissions by the end of the decade. As well as the dissenting votes there were also disruptions during the annual meeting from climate protestors.');



INSERT INTO comments VALUES (1, '11:19:06', 'One way or another your article is going to be awesome, I am pretty sure about it, but here is a question how much time it will take you?', 'Cellular', 1);
INSERT INTO comments VALUES (2, '02:36:17', 'Don’t even compare our situations with each other, my problems are a lot more complicated and awful.', 'Polli', 1);
INSERT INTO comments VALUES (3, '02:30:16', 'I hate it when people use emoji’s instead of answers, emoji’s make any conversation unserious.', 'Norman', 1);
INSERT INTO comments VALUES (4, '18:12:00', 'Zebra, why zebra has such a weird set of colors???', 'Curlicue', 1);
INSERT INTO comments VALUES (5, '01:30:45', 'I value information which you provide and always find your tips super useful.', 'Ignavia', 1);
INSERT INTO comments VALUES (6, '02:21:27', 'I have different priorities, it is okay you know, we don’t have to be all the same mate…', 'Sycosis', 1);
INSERT INTO comments VALUES (7, '04:33:08', 'I told you that new economy crisis is near, did you hear what happened to Deutsche Bank? They fired thousands of workers, it is a sign.', 'Conundrum', 1);
INSERT INTO comments VALUES (8, '18:56:40', 'I’m so happy, I took couple of painting lessons and I already feel like little Michelangelo.', 'Curlicue', 1);
INSERT INTO comments VALUES (9, '15:40:15', 'Truth is out there, boy I loved to watch X-files in my childhood, one of my favorites.', 'Jentacular', 1);
INSERT INTO comments VALUES (10, '18:05:43', 'Weeb memes are so funny, well done Japan, you are good at creating memes.', 'LimanYawp', 1);
INSERT INTO comments VALUES (11, '12:10:58', 'I double-checked my papers and I have no idea what is wrong with it, can you specify your issue please?', 'Cellular', 2);
INSERT INTO comments VALUES (12, '02:19:39', 'Handy blog, I always come here when I want to find some solution or guide.', 'Ignavia', 2);
INSERT INTO comments VALUES (13, '03:44:50', 'Hey guys, so I just joined your thread and I want to say that YOU GUYS ARE AWESOME!!!', 'Scribable', 2);
INSERT INTO comments VALUES (14, '20:04:05', 'Not now, in couple of years maybe, but not right now. Also let me know when release of your service is.', 'GantornGasbag', 2);
INSERT INTO comments VALUES (15, '15:15:00', 'Fantastic mood I’m having right now, Friday + vacation and salary D', 'Sycosis', 2);
INSERT INTO comments VALUES (16, '05:27:47', 'What about other issues? This one is resolved okay, but other issues are also crucial.', 'Conundrum', 2);
INSERT INTO comments VALUES (17, '08:10:00', 'Doing my maximum, it will take some time, but I think I will be able to fix this issue, just give me some time.', 'Curlicue', 2);
INSERT INTO comments VALUES (18, '12:10:58', 'What is wrong with people? They seem to be very aggressive and they can’t tolerate different point of view.', 'Jentacular', 2);
INSERT INTO comments VALUES (19, '20:34:13', 'How about we wait for him first, listen to his advice and make decision afterwards? Sounds good?', 'MenexVerily', 2);
INSERT INTO comments VALUES (20, '19:16:22', 'I just can''t stop playing puzzle games, no matter how many times I try it never gets boring.', 'Boleromorph511', 2);
INSERT INTO comments VALUES (21, '12:10:58', 'Details please, list more details, otherwise I can’t help you.', 'Scribable', 3);
INSERT INTO comments VALUES (22, '16:40:27', 'Which conspiracy theory is trending today? I’m wondering = D', 'Conundrum', 3);
INSERT INTO comments VALUES (23, '06:12:09', 'Found where you made mistake, tell me your e-mail and I will send you report.', 'Sycosis', 3);
INSERT INTO comments VALUES (24, '12:10:58', 'Poetry with sad music, rainy weather, dark clouds, and a cozy house is something out of this world.', 'Ignavia', 3);
INSERT INTO comments VALUES (25, '09:40:40', 'Some of my co-workers are dumb beyond impossibility and this is real struggle.', 'Curlicue', 3);
INSERT INTO comments VALUES (26, '09:03:10', 'I play Dota 2 and COD WW2, both are awesome, they eat all my free time HELP ME :D', 'LimanYawp', 3);
INSERT INTO comments VALUES (27, '18:08:36', 'Which phone is better how do you think? I think HTC is awesome and I hate Iphone |', 'Scribable', 3);
INSERT INTO comments VALUES (28, '12:10:58', 'Where can I get quality sensors? I have some project and I want to find reliable equipment.', 'Jentacular', 3);
INSERT INTO comments VALUES (29, '03:39:32', 'I value information which you provide and always find your tips super useful.', 'Cellular', 3);
INSERT INTO comments VALUES (30, '12:10:58', 'Guys, how do you keep yourself entertained during quarantine? What do you do?', 'Sycosis', 3);
INSERT INTO comments VALUES (31, '03:58:13', 'It’s a mad world out there and you can’t trust everyone, you have to keep own secrets you know.', 'Conundrum', 4);
INSERT INTO comments VALUES (32, '02:01:40', 'Changed my hard drive to SSD and my PC works faster than ever. I admire this SSD thing D', 'Ignavia', 4);
INSERT INTO comments VALUES (33, '16:47:42', 'Searching for solutions, still no progress so far, maybe we made mistake and we are not aware of it yet?', 'GantornGasbag', 4);
INSERT INTO comments VALUES (34, '10:14:59', 'Pin my comment, it was the only solution that actually worked, all other options failed.', 'Curlicue', 4);
INSERT INTO comments VALUES (35, '12:10:58', 'Is there any way to find out how many people play Overwatch?', 'Cellular', 4);
INSERT INTO comments VALUES (36, '23:52:55', 'Does anyone know some good horror movies? Please give me some recommendations :)', 'MenexVerily', 4);
INSERT INTO comments VALUES (37, '12:10:58', 'So what do you think about Sharingan? It would be awesome to have such power in real life, right?', 'Boleromorph511', 4);
INSERT INTO comments VALUES (38, '11:48:39', 'I want to buy a smartwatch, the problem is I hate to charge it every day.', 'Jentacular', 4);
INSERT INTO comments VALUES (39, '12:05:28', 'I hate my work, they blocked browser games and I can no longer enjoy them, they were so awesome…', 'Scribable', 4);
INSERT INTO comments VALUES (40, '22:12:03', 'No matter what, keep going forward, it is really hard I know, but try to focus on it.', 'Curlicue', 4);
INSERT INTO comments VALUES (41, '12:10:58', 'Sad music can actually have a positive impact on my mood, it is weird right?', 'LimanYawp', 5);
INSERT INTO comments VALUES (42, '07:31:48', 'Hats off to your writing skills mate, best which I have seen so far.', 'Sycosis', 5);
INSERT INTO comments VALUES (43, '04:58:16', 'How do you usually perform disk check? It takes too much time, is there any quicker way?', 'Conundrum', 5);
INSERT INTO comments VALUES (44, '05:38:25', 'Lonely, I feel kind of lonely, but I also prefer to keep the distance with people, what do you call such a case?', 'Ignavia', 5);
INSERT INTO comments VALUES (45, '12:10:58', 'Dude, stop making same mistake over and over again, how many times you can fail on the same step?', 'GantornGasbag', 5);
INSERT INTO comments VALUES (46, '11:55:41', 'I want to go to university which teaches how to become pro YouTuber or pro Blogger.', 'Curlicue', 5);
INSERT INTO comments VALUES (47, '12:10:58', 'Problem with your project is that, it looks like you haven''t put enough effort in it.', 'Cellular', 5);
INSERT INTO comments VALUES (48, '15:19:03', 'As soon as people reach success they lose their focus and motivation, success sometimes kills attitude.', 'Jentacular', 5);
INSERT INTO comments VALUES (49, '07:50:32', 'Getting things done and my project is almost over, can’t wait to show it to you.', 'Boleromorph511', 5);
INSERT INTO comments VALUES (50, '13:04:55', 'A lot of spooky news I see, as if there is looming crisis and we have no clue about it yet.', 'Sycosis', 5);
INSERT INTO comments VALUES (51, '17:33:43', 'I gave him chance before, I closed my eyes on his constant mistakes, but it can’t continue like this forever.', 'Scribable', 6);
INSERT INTO comments VALUES (52, '20:57:14', 'I enjoy classic music so much, it is so peaceful and beautiful.', 'Curlicue', 6);
INSERT INTO comments VALUES (53, '22:46:02', 'I hope I don’t have to repeat it all over again… take a pencil and write down each step please.', 'Ignavia', 6);
INSERT INTO comments VALUES (54, '12:10:58', 'Interesting guide author, but I prefer RPG games more than puzzle games.', 'LimanYawp', 6);
INSERT INTO comments VALUES (55, '04:16:32', 'Talked to my doctor and he said that you have the pressure of cosmonaut, anyone knows what does it mean?', 'Conundrum', 6);
INSERT INTO comments VALUES (56, '04:46:48', 'I mean this is ridiculous, it can’t be, there is no freaking way you can do it.', 'MenexVerily', 6);
INSERT INTO comments VALUES (57, '12:10:58', 'So what do you think about Valve Index? Is it good? Does it have a 144 Hz refresh rate?', 'Jentacular', 6);
INSERT INTO comments VALUES (58, '23:23:16', 'I prefer natural beauty over surgery, it is more pleasant personally for me.', 'Curlicue', 6);
INSERT INTO comments VALUES (59, '12:10:58', 'Being unique is so rare these days... people prefer to copy others...', 'Sycosis', 6);
INSERT INTO comments VALUES (60, '23:23:16', 'If I only knew about it 10 years ago, if I only knew it...', 'GantornGasbag', 6);
INSERT INTO comments VALUES (61, '10:54:50', 'Why I can bring old times back? Why I can’t feel same way as I used to? Where is solution to this problem?', 'Qasidane29', 7);
INSERT INTO comments VALUES (62, '10:44:56', 'Guys you have some grammar errors in your articles, use Microsoft Word for checking spelling.', 'Cellular', 7);
INSERT INTO comments VALUES (63, '08:32:18', 'So there was no happy end in GOT after all, I still liked it = D', 'Ignavia', 7);
INSERT INTO comments VALUES (64, '04:33:50', 'I have new hobby, filming movies with drones. I enjoy bird view so much.', 'Conundrum', 7);
INSERT INTO comments VALUES (65, '18:15:25', 'Mom jokes in online games are as common as noobs.', 'LimanYawp', 7);
INSERT INTO comments VALUES (66, '12:10:58', 'Looking for some suggestions, where should I spend my summer holidays? Any nice place around here?', 'Jentacular', 7);
INSERT INTO comments VALUES (67, '20:33:41', 'What about ASMR? Have you tried it out yet? I was able to fight my depression with it.', 'Curlicue', 7);
INSERT INTO comments VALUES (68, '17:37:33', 'I’m too tired today, I will bookmark your blog and read it tomorrow okay?', 'Tater', 7);
INSERT INTO comments VALUES (69, '12:10:58', 'There was something special about Cloud Atlas, some hidden meaning…', 'Scribable', 7);
INSERT INTO comments VALUES (70, '23:23:16', 'Anyone knows where can I find more blogs like this? Will tips you for information )', 'Boleromorph511', 7);
INSERT INTO comments VALUES (71, '10:54:50', 'I like how you are doing your job, very professional and no pointless talking or other BS.', 'Sycosis', 8);
INSERT INTO comments VALUES (72, '10:44:56', 'That awkward moment when you are introvert and you want some attention as well.', 'Qasidane29', 8);
INSERT INTO comments VALUES (73, '08:32:18', 'Do you guys think that aliens are really? That they are among us? Kind of spooky isn’t it?', 'Conundrum', 8);
INSERT INTO comments VALUES (74, '04:33:50', 'My dreams are different from yours, I want to explore universe and you think only about money.', 'GantornGasbag', 8);
INSERT INTO comments VALUES (75, '18:15:25', 'Got the virus the other day, boy it was a funny thing, my explorer kept restarting without stop.', 'Ignavia', 8);
INSERT INTO comments VALUES (76, '21:46:16', 'Fishing can be a very relaxing thing to do, it has a calming effect.', 'Curlicue', 8);
INSERT INTO comments VALUES (77, '20:33:41', 'I mean this is ridiculous, it can’t be, there is no freaking way you can do it.', 'Cellular', 8);
INSERT INTO comments VALUES (78, '17:37:33', 'Anyone knows good anime forums? I want to find friends which share my hobbies.', 'Jentacular', 8);
INSERT INTO comments VALUES (79, '12:10:58', 'I love to have deep, interesting conversations, it allows me to see life from a new perspective.', 'MenexVerily', 8);
INSERT INTO comments VALUES (80, '23:23:16', 'I want to know what Great Attractor is, curiosity is just killing me.', 'Sycosis', 8);
INSERT INTO comments VALUES (81, '10:54:50', 'Please don’t even mention it, I had so much trouble with it that I don’t even want to talk about it.', 'Scribable', 9);
INSERT INTO comments VALUES (82, '10:44:56', 'I cried so much on the end of Endgame, why? Just why he had to die?', 'LimanYawp', 9);
INSERT INTO comments VALUES (83, '08:32:18', 'IDK, it gets boring sometimes and I have no interest in it anymore.', 'Ignavia', 9);
INSERT INTO comments VALUES (84, '04:33:50', 'The game is really entertaining and interesting, I love the new engine as well, looks super awesome.', 'Jentacular', 9);
INSERT INTO comments VALUES (85, '18:15:25', 'Guys no matter what HODL, market will grow again and bull run will start, just have some patience.', 'Conundrum', 9);
INSERT INTO comments VALUES (86, '21:46:16', 'Too bad I learned about this so late, hope in the future I will remember to visit your blog.', 'Curlicue', 9);
INSERT INTO comments VALUES (87, '20:33:41', 'Awesome topic, whenever I get confused I come here and my problems are solved in no time.', 'Sycosis', 9);
INSERT INTO comments VALUES (88, '12:10:58', 'Can you guys bring updates more often, like couple of times per week would be awesome.', 'GantornGasbag', 9);
INSERT INTO comments VALUES (89, '17:37:33', 'Had this plan for a long time, but I never had opportunity to bring it to life.', 'Glabella', 9);
INSERT INTO comments VALUES (90, '23:23:16', 'Poetry with sad music, rainy weather, dark clouds, and a cozy house is something out of this world.', 'Scribable', 9);
INSERT INTO comments VALUES (91, '10:54:50', 'Not at all, the evening was nice and we discussed interesting topics, it was pretty good.', 'Boleromorph511', 10);
INSERT INTO comments VALUES (92, '10:44:56', 'Is there any way to improve my memory? I want to be able to remember big chunk of text.', 'Cellular', 10);
INSERT INTO comments VALUES (93, '08:32:18', 'Not at all, the evening was nice and we discussed interesting topics, it was pretty good.', 'LimanYawp', 10);
INSERT INTO comments VALUES (94, '04:33:50', 'Okay, all done, in case you have more questions visit other thread, this one will be closed soon.', 'Conundrum', 10);
INSERT INTO comments VALUES (95, '18:15:25', 'Quick question where do I order your service? I’m impresses, you guys are awesome.', 'Curlicue', 10);
INSERT INTO comments VALUES (96, '21:46:16', 'Found it, after all these years, after all of this search I have finally found it, THANKS!!!', 'Jentacular', 10);
INSERT INTO comments VALUES (97, '20:33:41', 'Guys you have some grammar errors in your articles, use Microsoft Word for checking spelling.', 'Glabella', 10);
INSERT INTO comments VALUES (98, '12:10:58', 'I’m searching for interesting person. Must be gamer, YouTuber and dreamer.', 'Sycosis', 10);
INSERT INTO comments VALUES (99, '17:37:33', 'We are not mad, we just need to find solution and I think you are the one who has to provide it.', 'Scribable', 10);
INSERT INTO comments VALUES (100, '23:23:16', 'Control your anger and your emotions well, otherwise, you will end up in bad situations.', 'GantornGasbag', 10);
INSERT INTO comments VALUES (101, '20:18:54', 'Do you guys think that aliens are really? That they are among us? Kind of spooky isn’t it?', 'Curlicue', 11);
INSERT INTO comments VALUES (102, '15:30:04', 'I do get tired and I do lose motivation from time to time, but I always come back and I always finish my work', 'Glabella', 11);
INSERT INTO comments VALUES (103, '14:35:22', 'From time to time I play Gunmayhem, the game is more than awesome and gameplay is enjoyable in it.', 'Sycosis', 11);
INSERT INTO comments VALUES (104, '02:30:16', 'Where do you get your facts? They usually are on point and I wanted to know your sources.', 'Cellular', 11);
INSERT INTO comments VALUES (105, '18:12:00', 'Long walks? Sure why not as far as I can listen to my favorite music.', 'GantornGasbag', 11);
INSERT INTO comments VALUES (106, '01:30:45', 'What is best method to kill time you say? Books, games, interesting work, sleep.', 'Jentacular', 11);
INSERT INTO comments VALUES (107, '02:21:27', 'I just came back to tell you that your advice worked well, I was able to fix the problem, thumbs up guys.', 'Curlicue', 11);
INSERT INTO comments VALUES (108, '04:33:08', 'You taught me so much in single article alone, I admire your writing skills.', 'LimanYawp', 11);
INSERT INTO comments VALUES (109, '18:56:40', 'Cool story bro, too bad it is all fiction and never took place in reality.', 'Conundrum', 11);
INSERT INTO comments VALUES (110, '15:40:15', 'Perfect blog doesn’t exist…. Oh never mind, your blog is pretty much perfect to me :D', 'MenexVerily', 11);
INSERT INTO comments VALUES (111, '20:18:54', 'All my friends are in relationship, is there something wrong with me?', 'Scribable', 12);
INSERT INTO comments VALUES (112, '15:30:04', 'I like this guy, he is funny and open-minded. If we had chance we could build a good friendship.', 'Sycosis', 12);
INSERT INTO comments VALUES (113, '14:35:22', 'Weird question, but who is author of this amazing post? Include name please.', 'Curlicue', 12);
INSERT INTO comments VALUES (114, '14:09:08', 'Some people share so many posts on the Facebook... Why are they doing it?', 'Glabella', 12);
INSERT INTO comments VALUES (115, '19:16:59', 'That moment when people call your everyday lifestyle quarantine, funny meme right?', 'Jentacular', 12);
INSERT INTO comments VALUES (116, '01:27:46', 'Can you guys please help with video rendering software? I am looking for the one that uses GPU instead of CPU.', 'Cellular', 12);
INSERT INTO comments VALUES (117, '16:30:14', 'My boss is a cool guy, he is chill all the time and it seems like he hardly cares about anything.', 'GantornGasbag', 12);
INSERT INTO comments VALUES (118, '12:05:42', 'Click bait is number one public enemy and number one cause of all problems.', 'Conundrum', 12);
INSERT INTO comments VALUES (119, '00:02:30', 'I love to visit museums from time to time, touching old history is fascinating.', 'Curlicue', 12);
INSERT INTO comments VALUES (120, '12:10:58', 'Job done, I fixed all your problems. Was kind of hard to handle third bug, but I managed to resolve it.', 'LimanYawp', 12);
INSERT INTO comments VALUES (121, '20:18:54', 'Joining your community guys, hope you are all doing well.', 'Boleromorph511', 13);
INSERT INTO comments VALUES (122, '15:30:04', 'So what do you think about the new royal haki of Luffy? It was pretty impressive, right?', 'Sycosis', 13);
INSERT INTO comments VALUES (123, '14:35:22', 'I enjoy cloudy weather, I enjoy storms, I enjoy rains, I enjoy darkness in general, but I’m not bad or evil.', 'Scribable', 13);
INSERT INTO comments VALUES (124, '14:09:08', 'I don''t think that life ends with death, it is kind of boring. I believe that there is something else after death.', 'Glabella', 13);
INSERT INTO comments VALUES (125, '19:16:59', 'I really don''t like it when people pull out their phones during conversations and start to stare at the screen.', 'Jentacular', 13);
INSERT INTO comments VALUES (126, '01:27:46', 'I have right to demand vacation right? And if I’m not getting it in time, it is not legal right?', 'Qasidane29', 13);
INSERT INTO comments VALUES (127, '16:30:14', 'Pointing out that your solution works only on old versions, new ones will fail.', 'Curlicue', 13);
INSERT INTO comments VALUES (128, '12:05:42', 'I love so much to listen to music while driving, classic songs, opera, etc. This is pure pleasure.', 'MenexVerily', 13);
INSERT INTO comments VALUES (129, '00:02:30', 'Thirsty teenagers got triggered by Belle Delphine, good, good D', 'LimanYawp', 13);
INSERT INTO comments VALUES (130, '12:10:58', 'How can I find a difference in this situation? From my point of view, they are both the same.', 'Glabella', 13);
INSERT INTO comments VALUES (131, '12:10:58', 'Does anyone have HTC Vive? I want to know how good it is and what is your experience with it.', 'Sycosis', 14);
INSERT INTO comments VALUES (132, '12:10:58', 'I can no longer feel love same way as I used to in my childhood, things have changed, in a bad way…', 'Scribable', 14);
INSERT INTO comments VALUES (133, '12:10:58', 'I mean this is ridiculous, it can’t be, there is no freaking way you can do it.', 'Conundrum', 14);
INSERT INTO comments VALUES (134, '14:09:08', 'Vast majority of people don''t think about anything else but, fame, money, power.', 'Jentacular', 14);
INSERT INTO comments VALUES (135, '19:16:59', 'Productivity is the most important thing when it comes to building a business, keep an eye on it.', 'GantornGasbag', 14);
INSERT INTO comments VALUES (136, '01:27:46', 'Can you imagine how helpful it was? Fixed problem instantly after suffering from it for months.', 'Curlicue', 14);
INSERT INTO comments VALUES (137, '16:30:14', 'Kids these days are not as active as we used to be in the past, it can cause pretty bad health issues.', 'Cellular', 14);
INSERT INTO comments VALUES (138, '12:05:42', 'Him? Friendly? You really don’t know him well do you? He is an awful person.', 'Sycosis', 14);
INSERT INTO comments VALUES (139, '00:02:30', 'When I sit for a long time in front of monitor my eyes start to hurt, is there any solution?', 'Conundrum', 14);
INSERT INTO comments VALUES (140, '13:46:25', 'Wait a minute, your article gave me amazing idea and I won’t sleep until I will give it a try.', 'Boleromorph511', 14);
INSERT INTO comments VALUES (141, '20:18:54', 'I just can’t stop playing zombie games, I love Dying Light so much, it is epic game guys, try it.', 'Glabella', 15);
INSERT INTO comments VALUES (142, '15:30:04', 'Had this plan for a long time, but I never had opportunity to bring it to life.', 'Tater', 15);
INSERT INTO comments VALUES (143, '14:35:22', 'I can’t get enough of animes which are about main characters trapped in video games.', 'Jentacular', 15);
INSERT INTO comments VALUES (144, '14:09:08', 'Guys, how do you usually kill boredom at work? DO you prefer YouTube, Coub, Tik Tok or games?', 'Scribable', 15);
INSERT INTO comments VALUES (145, '19:16:59', 'The last boss is impossible to beat, I can’t think of any possible strategies, nothing works.', 'Sycosis', 15);
INSERT INTO comments VALUES (146, '01:27:46', 'Erm what? WHAT? HOW? Sorry for caps, but this advice is so simple and yet it works.', 'Cellular', 15);
INSERT INTO comments VALUES (147, '16:30:14', 'Increase the font size on the blog please, I can hardly read and it is painful for my eyes.', 'LimanYawp', 15);
INSERT INTO comments VALUES (148, '12:05:42', 'Cute, you are so cute, especially when you are trying to explain why you did it.', 'Qasidane29', 15);
INSERT INTO comments VALUES (149, '12:10:58', 'My favorite browser game? Of course, it is Earn to Die game series, I love survival games very much.', 'Curlicue', 15);
INSERT INTO comments VALUES (150, '13:46:25', 'Yawn, reading articles is boring, I prefer more watching video guides.', 'MenexVerily', 15);
INSERT INTO comments VALUES (151, '20:18:54', 'I like people with good sense of humor, they are funny and pleasant to talk with.', 'Glabella', 16);
INSERT INTO comments VALUES (152, '15:30:04', 'Basic education is so important, yet our education system completely ignores it.', 'Jentacular', 16);
INSERT INTO comments VALUES (153, '14:35:22', 'I can’t trust any government funded news outlets, I need to find independent journalists somewhere.', 'Boleromorph511', 16);
INSERT INTO comments VALUES (154, '14:09:08', 'Once one wise person said that bad peace is always better than good war and he was right about it.', 'Conundrum', 16);
INSERT INTO comments VALUES (155, '19:16:59', 'Healers taking your agro is worst kind of scenario for Tank...', 'Curlicue', 16);
INSERT INTO comments VALUES (156, '01:27:46', 'That awkward moment when you are introvert and you want some attention as well.', 'Scribable', 16);
INSERT INTO comments VALUES (157, '16:30:14', 'What has become with movie industry? Nothing, but special effects, were is interesting storyline?', 'Sycosis', 16);
INSERT INTO comments VALUES (158, '12:05:42', 'No one has ever become poor by giving.', 'GantornGasbag', 16);
INSERT INTO comments VALUES (159, '12:10:58', 'Unreal quality of the image, love details, love colors and everything.', 'LimanYawp', 16);
INSERT INTO comments VALUES (160, '13:46:25', 'If only I could visit other galaxies, other planets and explore new worlds...', 'Glabella', 16);
INSERT INTO comments VALUES (161, '20:18:54', 'Sad to see that youngsters prefer to sit next to computer all day long instead of playing outside.', 'Cellular', 17);
INSERT INTO comments VALUES (162, '15:30:04', 'I liked it very much, we even ordered a second round and evening was awesome in the end.', 'Jentacular', 17);
INSERT INTO comments VALUES (163, '14:35:22', 'Anyone knows here Photoshop? I need some small help, won’t take a long time I promise = D', 'Conundrum', 17);
INSERT INTO comments VALUES (164, '14:09:08', 'I prefer natural beauty over surgery, it is more pleasant personally for me.', 'Curlicue', 17);
INSERT INTO comments VALUES (165, '19:16:59', 'Waiting sucks, I hate when I have to wait for whole squad to come online, so we can start game.', 'Scribable', 17);
INSERT INTO comments VALUES (166, '01:27:46', 'I hate it when they are late with salaries, I hate it so much... Had such problem today again...', 'Sycosis', 17);
INSERT INTO comments VALUES (167, '16:30:14', 'Not so impressive to me and I already knew about it D', 'Qasidane29', 17);
INSERT INTO comments VALUES (168, '12:05:42', 'Unity player? What is it? Do I have to install it in order to play your game? Where can I find it?', 'GantornGasbag', 17);
INSERT INTO comments VALUES (169, '12:10:58', 'I’m kind of in the middle of something, wait for a bit, I will contact you shortly.', 'MenexVerily', 17);
INSERT INTO comments VALUES (170, '00:32:54', 'Guys, did you see a new Witcher trailer? It looks really awesome, right?', 'Cellular', 17);
INSERT INTO comments VALUES (171, '10:43:41', 'Guys take care of your health, throw away bad habits and follow healthy lifestyle.', 'Boleromorph511', 18);
INSERT INTO comments VALUES (172, '14:10:06', 'Winter is awesome, I don’t mind cold at all, it is the heat that stresses me a lot, cold is fine.', 'Jentacular', 18);
INSERT INTO comments VALUES (173, '04:43:43', 'Just like you told me, changing font solved all problems, thank you, kind sir, for your help.', 'LimanYawp', 18);
INSERT INTO comments VALUES (174, '11:57:00', 'Singing in the shower is one of the best remedies from stress, I do it every evening and it helps me a lot.', 'Curlicue', 18);
INSERT INTO comments VALUES (175, '04:11:58', 'I started to appreciate holidays more, one I got a full-time job, funny isn’t it?', 'Glabella', 18);
INSERT INTO comments VALUES (176, '13:59:54', 'So guys, what do you think about new GOT official mobile game? It looks really awesome right?', 'Tater', 18);
INSERT INTO comments VALUES (177, '07:28:37', 'Trust issues? Sure I have it, but I call it common sense, you can trust people these days.', 'Sycosis', 18);
INSERT INTO comments VALUES (178, '12:10:58', 'I need really high quality sleep, 10 hours or so would be nice D', 'Conundrum', 18);
INSERT INTO comments VALUES (179, '05:39:26', 'Let’s plan our vacation together, I will talk with my boss and we can together make some plans okay?', 'GantornGasbag', 18);
INSERT INTO comments VALUES (180, '00:32:54', 'Comment section of 9GAG is priceless, I follow this page for comments only D', 'Scribable', 18);
INSERT INTO comments VALUES (181, '10:43:41', 'Pretty interesting solution, what about already deleted driver files which can’t be recovered?', 'Qasidane29', 19);
INSERT INTO comments VALUES (182, '14:10:06', 'Be careful how you choose your friends, the wrong choice can have bad consequences.', 'LimanYawp', 19);
INSERT INTO comments VALUES (183, '04:43:43', 'Share kindness, inspire people, encourage them, show your positive attitude and life will reward you.', 'Curlicue', 19);
INSERT INTO comments VALUES (184, '11:57:00', 'You need better gaming skills if you want to solve this level, it is hard and it is complicated.', 'Jentacular', 19);
INSERT INTO comments VALUES (185, '04:11:58', 'I wish people cared as much about the Earth as they did about who they think created it.', 'Cellular', 19);
INSERT INTO comments VALUES (186, '13:59:54', 'How is that even possible? I always thought that inbuilt software should prevent it automatically.', 'Scribable', 19);
INSERT INTO comments VALUES (187, '07:28:37', 'My nightmare? It was kind of weird and kind of fantastic as well, I can say that I have witnessed WWIII.', 'Conundrum', 19);
INSERT INTO comments VALUES (188, '12:10:58', 'If I had a chance to choose one superpower it would be the ability to teleport, I love traveling D', 'Sycosis', 19);
INSERT INTO comments VALUES (189, '05:39:26', 'Make sure that you don’t overheat your PC during summer, check temperature and clean case from dust.', 'Curlicue', 19);
INSERT INTO comments VALUES (190, '00:32:54', 'How about I now leave chat and enjoy my time sleeping in warm bad D', 'Glabella', 19);
INSERT INTO comments VALUES (191, '10:43:41', 'Some epic news I have for you guys, we will soon see Cyber Punk!!!', 'Cellular', 20);
INSERT INTO comments VALUES (192, '14:10:06', 'What can I say, well done my friend, you did it again, you are awesome.', 'MenexVerily', 20);
INSERT INTO comments VALUES (193, '04:43:43', 'Geopolitical situation is changing very fast, we are heading towards multipolar world.', 'Scribable', 20);
INSERT INTO comments VALUES (194, '11:57:00', 'At least I still have my games, I can forget about her more easily now…', 'GantornGasbag', 20);
INSERT INTO comments VALUES (195, '04:11:58', 'I’m not really into coffee to be honest, I more prefer tea and other natural drinks.', 'Sycosis', 20);
INSERT INTO comments VALUES (196, '13:59:54', 'At day I don’t believe in ghosts, at night I’m little bit more open minded. Funny right? = D', 'Glabella', 20);
INSERT INTO comments VALUES (197, '07:28:37', 'Anyone knows what kind of stats should good rendering PC have?', 'Conundrum', 20);
INSERT INTO comments VALUES (198, '12:10:58', 'Oh dear, just look at all of this fake news around us. Where can I find reliable news source?', 'LimanYawp', 20);
INSERT INTO comments VALUES (199, '05:39:26', 'Saw the other day vogue dance, it is weird, but I really loved moves in it.', 'Curlicue', 20);
INSERT INTO comments VALUES (200, '12:10:58', 'Been there, tried that, no point, it won’t work no matter what, I’m tired....', 'Jentacular', 20);


DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    userName VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL
);


DROP TABLE IF EXISTS roles;
CREATE TABLE roles
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);


DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (user_id, role_id)
);


INSERT INTO public.users(id, userName, password, email)
VALUES (1, 'admin1', '123', 'mail1@mail.com');
INSERT INTO public.users(id, userName, password, email)
VALUES (2, 'admin2', '123', 'mail2@mail.com');
INSERT INTO public.users(id, userName, password, email)
VALUES (3, 'journalist1', '123', 'mail3@mail.com');
INSERT INTO public.users(id, userName, password, email)
VALUES (4, 'journalist2', '123', 'mail4@mail.com');
INSERT INTO public.users(id, userName, password, email)
VALUES (5, 'subscriber1', '123', 'mail5@mail.com');
INSERT INTO public.users(id, userName, password, email)
VALUES (6, 'subscriber2', '123', 'mail6@mail.com');


INSERT INTO roles(id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles(id, name) VALUES (2, 'ROLE_JOURNALIST');
INSERT INTO roles(id, name) VALUES (3, 'ROLE_SUBSCRIBER');


INSERT INTO public.users_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO public.users_roles(user_id, role_id) VALUES (2, 1);
INSERT INTO public.users_roles(user_id, role_id) VALUES (3, 2);
INSERT INTO public.users_roles(user_id, role_id) VALUES (4, 2);
INSERT INTO public.users_roles(user_id, role_id) VALUES (5, 3);
INSERT INTO public.users_roles(user_id, role_id) VALUES (6, 3);
