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
INSERT INTO news VALUES (2, '14:35:10', 'Discord leaker: Catch up on the new allegations', 'CNN — It’s been two weeks since 21-year-old Jack Teixeira was arrested in shorts in front of his mother’s house, accused of leaking a trove of classified documents over a period of months to a closed online community on Discord, a social media platform popular with video gamers.');
INSERT INTO news VALUES (3, '13:07:46', 'States are moving in polar opposite directions on guns', 'CNN - With almost all gun legislation a nonstarter on a divided Capitol Hill, states are charting wildly divergent paths. Some states are doing everything they can to restrict access to certain weapons. Others are doing everything they can to make guns as accessible as possible without much government oversight. On the restriction side, Washington on Tuesday became the 10th state to impose an assault-weapons ban, signaling new movement among states to step in where the federal government has failed. The national assault-weapons ban, which was in place for 10 years, lapsed in 2004.');
INSERT INTO news VALUES (4, '02:43:01', 'Election 2024: A consequential rematch of serial candidates', 'CNN — This could be the most consequential rematch in US history – a president who portrays himself as the champion of democracy against a former president who routinely rejects election results. Anyone fearing Donald Trump’s return and those who reject Joe Biden’s victory could agree that nothing less than the future of the republic is at stake if Biden and Trump are on the ballot in 2024. Biden explicitly made that case in his announcement video on Tuesday, saying “we still are” in a “battle for the soul of America.”');
INSERT INTO news VALUES (5, '21:14:46', 'McCarthy’s victory on debt ceiling vote may be short-lived', 'CNN — Speaker Kevin McCarthy passed his toughest test yet by buying off holdout Republicans and squeezing a bill with huge spending cuts through the House on Wednesday. But the price of proving his authority with a radical GOP conference was pushing the nation closer to a debt default cliff edge in an increasingly alarming showdown with President Joe Biden. The bill, which raises the debt ceiling for a year in return for spending cuts, was designed to hike pressure on Biden, who is refusing to accept Republican conditions that would effectively gut his domestic agenda. Unless the debt ceiling is lifted by the summer, the economy could crash.');
INSERT INTO news VALUES (6, '15:08:22', 'Billionaire GOP donor says DeSantis won''t return his calls', 'John Catsimatidis, the billionaire CEO of the Gristedes supermarket chain who donated to Donald Trump''s 2020 re-election campaign, says that Florida Gov. Ron DeSantis isn''t returning his calls despite DeSantis eying a 2024 presidential run.');
INSERT INTO news VALUES (7, '08:26:02', 'We are already in the food fight portion of the GOP primary', 'CNN — The 2024 Republican presidential primary is not fully underway as yet and already we are in the food fight phase. A super PAC supporting former President Donald Trump tried to smear Florida Gov. Ron DeSantis with pudding, seizing on a report, which the governor denies, about his eating habits to make a point about Social Security and Medicare. The ad itself is gross. And it drew a super PAC supporting DeSantis off the sidelines to air an ad of its own wondering why Trump was going after the Florida governor.');
INSERT INTO news VALUES (8, '18:24:12', 'Sexual assaults in the US military increased by 1% last year', 'CNN — The US military saw a 1% increase in sexual assaults last year, according to the Pentagon’s latest annual report. There were 7,378 reports of sexual assault against service members in 2022, according to the Fiscal Year 2022 Annual Report on Sexual Assault in the Military, released on Thursday. That is up from 7,260 reports of assault in 2021. All of the services aside from the Army saw an increase in reports from last year, officials said during a briefing on the report on Thursday: the Navy, Marine Corps, and Air Force saw a 9%, 3.6%, and 13% increase in reports, respectively. The Army, meanwhile, saw a 9% decrease.');
INSERT INTO news VALUES (9, '21:18:02', 'US Army renames Fort Lee after 2 pioneering Black Army officers', 'CNN  — The US Army renamed Virginia’s Fort Lee military base Thursday in honor of two Black service members, following a branch-wide push to rename bases named after Confederate leaders. The base will be “redesignated” as Fort Gregg-Adams after Lt. Gen. Arthur Gregg and Lt. Col. Charity Adams, the Army announced on Twitter. It had been named after Gen. Robert E. Lee, who led Confederate forces in the American Civil War. “Both Soldiers excelled in the field of sustainment and made significant contributions to #USArmy history,” the Army said in a tweet. “We’re proud to honor the courage, sacrifice, and diversity of these distinguished Soldiers and also our civilians.”');
INSERT INTO news VALUES (10, '17:31:16', 'Senate fails to advance Equal Rights Amendment resolution', 'CNN — The Senate on Thursday failed to advance a resolution to remove the deadline for ratification of the Equal Rights Amendment, a proposed amendment to the Constitution. The resolution failed 51-47. Although it had two Republican co-sponsors – Sens. Lisa Murkowski of Alaska and Susan Collins of Maine – most GOP senators opposed it. Republicans have generally argued they don’t think the amendment is needed because of the equal protections provided to women in the 14th Amendment. Supporters, however, say the ERA would ban discrimination on the basis of sex and guarantee equality for all under the Constitution');


INSERT INTO comments VALUES (1, '11:19:06', 'Text1', 'user1', 1);
INSERT INTO comments VALUES (2, '02:36:17', 'Text1', 'user2', 2);
INSERT INTO comments VALUES (3, '02:30:16', 'Text1', 'user3', 3);
INSERT INTO comments VALUES (4, '18:12:00', 'Text1', 'user4', 4);
INSERT INTO comments VALUES (5, '01:30:45', 'Text1', 'user5', 2);
INSERT INTO comments VALUES (6, '02:21:27', 'Text1', 'user6', 1);
INSERT INTO comments VALUES (7, '04:33:08', 'Text1', 'user7', 5);
INSERT INTO comments VALUES (8, '18:56:40', 'Text1', 'user8', 6);
INSERT INTO comments VALUES (9, '15:40:15', 'Text1', 'user9', 8);
INSERT INTO comments VALUES (10, '12:10:58', 'Text1', 'user10', 7);