DROP TABLE IF EXISTS TOPIC CASCADE;
-- Create the TOPIC table
CREATE TABLE TOPIC (
    id int8 generated always as identity,
    header VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO TOPIC (header, description) VALUES
    ('Passive Voice (受身形)', 'Learn how to use the passive form in Japanese to indicate actions done by someone else. Example: 食べられる (taberareru) - to be eaten.'),
    ('Te-Form (て形)', 'Master the te-form, which is essential for connecting verbs and forming various grammatical patterns. Example: 食べて (tabete) - eat and...'),
    ('Keigo (敬語)', 'Understand the honorific language used to show respect in Japanese, including 尊敬語 (sonkeigo) and 謙譲語 (kenjōgo).'),
    ('Casual and Polite Forms', 'Switch between the casual and polite forms of Japanese, such as です/ます versus dictionary forms. Example: 食べる (taberu) vs. 食べます (tabemasu).'),
    ('Conjugation of Adjectives', 'Learn to conjugate Japanese adjectives, including い-adjectives and な-adjectives. Example: 高い (takai) - high, 高くない (takakunai) - not high.'),
    ('Particles (助詞)', 'Understand the role of particles like が, を, で, に, and their uses in sentences. Example: 猫がいる (neko ga iru) - There is a cat.'),
    ('Causative Form (使役形)', 'Study how to express causation, such as making someone do something. Example: 食べさせる (tabesaseru) - to make someone eat.'),
    ('Conditionals (条件形)', 'Explore how to express conditions using forms like と, ば, たら, and なら. Example: 雨が降ったら、行きません (ame ga futtara, ikimasen) - If it rains, I won’t go.'),
    ('Negative Forms', 'Learn how to make negative forms of verbs, adjectives, and nouns. Example: 食べない (tabenai) - not eat.'),
    ('Relative Clauses', 'Understand how to use relative clauses to modify nouns. Example: 私が好きな本 (watashi ga suki na hon) - the book I like.');
