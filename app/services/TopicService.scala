package services

import javax.inject.{Inject, Singleton}

import exceptions.KnownSystemException
import models.{Topic, TopicRepository}
import play.api.mvc.{AnyContent, Request}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

/**
  * Created by sheep3 on 2017/10/21.
  */
@Singleton
class TopicService @Inject()(topicRepository: TopicRepository) {
  def addTopic(name: String, desc: String, user_id: Long)(implicit request: Request[AnyContent]) = {
    topicRepository.findByName(name).flatMap {
      case Some(_) =>
        throw new KnownSystemException("该主题已存在")
      case _ =>
        topicRepository.save(new Topic(name = name, desc = desc, user_id = user_id))
    }
  }


  def list(page: Int = 0, pageSize: Int = 10, filter: String = "%") = {
    topicRepository.list(page, pageSize, filter)
  }

  def followList(user_id: Long) = {
    topicRepository.followList(user_id)
  }

  def listWithUser(user_id: Long, page: Int = 0, pageSize: Int = 10, filter: String = "%") = {
    topicRepository.listWithUser(user_id, page, pageSize, filter)
  }


  def follow(topic_id: Long, user_id: Long) = {
    topicRepository.findByTopicId(topic_id).flatMap {
      case Some(_) =>
        topicRepository.follow(topic_id, user_id)
      case _ =>
        throw new KnownSystemException("该主题不存在")
    }
  }

  def unFollow(topic_id: Long, user_id: Long) = {
    topicRepository.findByTopicId(topic_id).flatMap {
      case Some(_) =>
        topicRepository.unFollow(topic_id, user_id)
      case _ =>
        throw new KnownSystemException("该主题不存在")
    }
  }
}
