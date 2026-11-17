-- The users table was never used by the application (it only held a seeded demo admin).
DROP TABLE IF EXISTS users CASCADE;

-- Keep one row per YouTube video before adding the unique constraint (duplicates came from parallel requests).
DELETE FROM individual_subtitle_translation
WHERE subtitle_id IN (
    SELECT vs.id FROM video_subtitle vs
    JOIN video_metadata vm ON vm.id = vs.video_id
    WHERE vm.id NOT IN (SELECT MIN(id) FROM video_metadata GROUP BY youtube_id)
);
DELETE FROM video_subtitle
WHERE video_id NOT IN (SELECT MIN(id) FROM video_metadata GROUP BY youtube_id);
DELETE FROM video_metadata
WHERE id NOT IN (SELECT MIN(id) FROM video_metadata GROUP BY youtube_id);

ALTER TABLE video_metadata ALTER COLUMN youtube_id SET NOT NULL;
ALTER TABLE video_metadata ADD CONSTRAINT uk_video_metadata_youtube_id UNIQUE (youtube_id);
