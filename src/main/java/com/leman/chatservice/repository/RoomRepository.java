package com.leman.chatservice.repository;

import com.leman.chatservice.entity.Room;
import com.leman.chatservice.enums.RoomType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

    @Query("""
            SELECT DISTINCT r FROM Room r
            JOIN r.members m
            WHERE m.user.id = :userId
            AND (CAST(:name AS string) IS NULL OR r.name ILIKE CONCAT('%', CAST(:name AS string), '%'))
            AND (:type IS NULL OR r.type = :type)
            """)
    Page<Room> findAllByMember(@Param("userId") Long userId,
                               @Param("name") String name,
                               @Param("type") RoomType type,
                               Pageable pageable);

}
