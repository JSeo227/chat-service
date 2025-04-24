package com.example.chat.chat_service.repository.memory;

import com.example.chat.chat_service.domain.Member;
import com.example.chat.chat_service.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Primary
public class MemoryMemberRepository implements MemberRepository {

    private static long sequence = 0L;
    private static final Map<Long, Member> store = new HashMap<Long, Member>();

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Member> findByName(String name) {
        return store.values().stream()
                .filter(m -> m.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clear() {
        store.clear();
    }
}
