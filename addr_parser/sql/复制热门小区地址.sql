truncate  popular_residence_address;

INSERT INTO popular_residence_address(ref_id,
                                                  is_deal,
                                                  address,
                                                  ori_room_no,
                                                  area,
                                                  residence_id,
                                                  residence,
                                                  building,
                                                  room,
                                                  is_available,
                                                  parsed_score,
                                                  src,
                                                  last_parsed_time)
   SELECT a.ref_id,
          a.is_deal,
          a.address,
          a.ori_room_no,
          a.area,
          a.residence_id,
          a.residence,
          a.building,
          a.room,
          a.is_available,
          a.parsed_score,
          a.src,
          a.last_parsed_time
     FROM address_unique a
          INNER JOIN popular_residence p ON a.residence_id = p.residence_id
    WHERE a.parsed_score < 60