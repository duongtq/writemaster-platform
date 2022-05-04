package com.writemaster.platform.service.impl;

import com.writemaster.platform.dto.LikeDTO;
import com.writemaster.platform.entity.Author;
import com.writemaster.platform.entity.Like;
import com.writemaster.platform.entity.LikePK;
import com.writemaster.platform.entity.Post;
import com.writemaster.platform.exception.AuthorNotFoundException;
import com.writemaster.platform.exception.PostAlreadyLikedException;
import com.writemaster.platform.exception.PostNotFoundException;
import com.writemaster.platform.exception.PostNotLikedYetException;
import com.writemaster.platform.mapper.LikeMapper;
import com.writemaster.platform.repository.AuthorRepository;
import com.writemaster.platform.repository.LikeRepository;
import com.writemaster.platform.repository.PostRepository;
import com.writemaster.platform.service.LikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {
  private final LikeRepository likeRepository;
  private final PostRepository postRepository;
  private final AuthorRepository authorRepository;
  private final LikeMapper likeMapper;

  public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, AuthorRepository authorRepository, LikeMapper likeMapper) {
    this.likeRepository = likeRepository;
    this.postRepository = postRepository;
    this.authorRepository = authorRepository;
    this.likeMapper = likeMapper;
  }

  @Override
  @Transactional
  public LikeDTO likePost(Integer authorId, Integer postId) {
    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }
    
    Like liked = likeRepository.findByAuthorIdAndPostId(authorId, postId);
    
    if (Optional.ofNullable(liked).isPresent()) {
    	if (liked.getLiked().equals(Boolean.FALSE)) {
    		post.setNumberOfLikes(post.getNumberOfLikes() + 1);
        postRepository.save(post);

        liked.setLiked(Boolean.TRUE);
        return likeMapper.fromEntity(likeRepository.save(liked));
    	}
    	else {
    	  throw new PostAlreadyLikedException(String.format("Post id %d already liked by author id %d", postId, authorId));
    	}
    }

    post.setNumberOfLikes(post.getNumberOfLikes() + 1);
    postRepository.save(post);

    Like like = new Like();
    like.setLikePK(new LikePK(author.getId(), post.getId()));
    like.setAuthor(author);
    like.setPost(post);
    like.setLiked(Boolean.TRUE);

    return likeMapper.fromEntity(likeRepository.save(like));
  }

  @Override
  @Transactional
  public LikeDTO unlikePost(Integer authorId, Integer postId) {

    Author author = authorRepository.findAuthorById(authorId);
    if (Optional.ofNullable(author).isEmpty()) {
      throw new AuthorNotFoundException("No author of id " + authorId);
    }
    Post post = postRepository.findPostById(postId);
    if (Optional.ofNullable(post).isEmpty()) {
      throw new PostNotFoundException("No post of id " + postId);
    }
    
    Like like = likeRepository.findByAuthorIdAndPostId(authorId, postId);

    if (Optional.ofNullable(like).isPresent() && post.getNumberOfLikes() > 0) {  	
  		post.setNumberOfLikes(post.getNumberOfLikes() - 1);
  		postRepository.save(post);
    } else {
    	throw new PostNotLikedYetException("To unlike, you should like it first. :D");
    }
    
    like.setLiked(Boolean.FALSE);
    return likeMapper.fromEntity(likeRepository.save(like));
  }

  @Override
  public LikeDTO getLikeByAuthorAndPost(Integer authorId, Integer postId) {
    Like like = likeRepository.findByAuthorIdAndPostId(authorId, postId);
    return likeMapper.fromEntity(like);
  }
}
